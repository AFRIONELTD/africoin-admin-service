package com.afrione.africoinservice.domain.services;

import com.afrione.africoinservice.domain.entities.AbstractBaseEntity;
import com.afrione.africoinservice.domain.entities.AuditTrailEntity;
import com.afrione.africoinservice.domain.models.dao.search.AuditTrailSearchDTO;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import org.springframework.data.domain.Page;

/**
 * Created by jnya on
 * Mon, 01 Sept, 2025
 */
public interface AuditService {

    enum ActionType {
        CREATE, UPDATE, DELETE;
    }
    <T extends AbstractBaseEntity<Long>> void createAudit(AuthenticatedUser authenticatedUser, T entity, ActionType actionType, String actionDescription);

    <T extends AbstractBaseEntity<Long>> void createAudit(T entity, ActionType actionType, String actionDescription);

    Page<AuditTrailEntity> searchRecords(AuditTrailSearchDTO searchDTO, int page, int size);
}
