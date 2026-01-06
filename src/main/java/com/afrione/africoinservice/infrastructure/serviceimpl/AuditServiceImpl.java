package com.afrione.africoinservice.infrastructure.serviceimpl;

import com.afrione.africoinservice.domain.entities.AbstractBaseEntity;
import com.afrione.africoinservice.domain.entities.AuditTrailEntity;
import com.afrione.africoinservice.domain.entities.enums.AuditActionTypeConstant;
import com.afrione.africoinservice.domain.models.dao.search.AuditTrailSearchDTO;
import com.afrione.africoinservice.domain.services.AuditService;
import com.afrione.africoinservice.infrastructure.persistence.repository.AuditTrailEntityRepository;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditTrailEntityRepository auditTrailEntityRepository;

    @Override
    public <T extends AbstractBaseEntity<Long>> void createAudit(AuthenticatedUser authenticatedUser, T entity, ActionType actionType, String actionDescription) {
        try {
            AuditTrailEntity auditTrail = AuditTrailEntity.builder()
                    .actionType(AuditActionTypeConstant.valueOf(actionType.name()))
                    .actorId(authenticatedUser.getUserId())
                    .accountId(authenticatedUser.getAccountId())
                    .actionUserName(authenticatedUser.getUsername())
                    .description(actionDescription)
                    .entityId(entity.getId())
                    .entityName(entity.getClass().getSimpleName())
                    .build();

            auditTrailEntityRepository.save(auditTrail);
            log.info("Audit trail created successfully for user: {} with action: {}", authenticatedUser.getUsername(), actionType);
        } catch (Exception e) {
            log.error("Error creating audit trail for user: {} with action: {}", authenticatedUser.getUsername(), actionType, e);
            // Don't throw exception to prevent audit failure from affecting main operation
        }
    }

    @Override
    public <T extends AbstractBaseEntity<Long>> void createAudit(T entity, ActionType actionType, String actionDescription) {
        try {
            AuditTrailEntity auditTrail = AuditTrailEntity.builder()
                    .actionType(AuditActionTypeConstant.valueOf(actionType.name()))
                    .description(actionDescription)
                    .entityId(entity.getId())
                    .entityName(entity.getClass().getSimpleName())
                    .build();

            auditTrailEntityRepository.save(auditTrail);
            log.info("Audit trail created successfully for entity: {} with action: {}", entity.getClass().getSimpleName(), actionType);
        } catch (Exception e) {
            log.error("Error creating audit trail for entity: {} with action: {}", entity.getClass().getSimpleName(), actionType, e);
            // Don't throw exception to prevent audit failure from affecting main operation
        }
    }

    @Override
    public Page<AuditTrailEntity> searchRecords(AuditTrailSearchDTO searchDTO, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<AuditTrailEntity> specification = buildSpecification(searchDTO);
        return auditTrailEntityRepository.findAll(specification, pageable);
    }

    private Specification<AuditTrailEntity> buildSpecification(AuditTrailSearchDTO searchDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchDTO.getFromTime() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreated"), searchDTO.getFromTime()));
            }

            if (searchDTO.getToTime() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateCreated"), searchDTO.getToTime()));
            }

            if (searchDTO.getActionType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("actionType"), searchDTO.getActionType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
