package com.afrione.africoinservice.infrastructure.serviceimpl;

import com.afrione.africoinservice.domain.dao.PrivilegeEntityDao;
import com.afrione.africoinservice.domain.entities.PrivilegeEntity;
import com.afrione.africoinservice.domain.entities.enums.PrivilegeTypeConstant;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.domain.services.PrivilegeValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrivilegeValidationServiceImpl implements PrivilegeValidationService {

    private final PrivilegeEntityDao privilegeEntityDao;

    @Override
    public boolean validatePrivilegeConsistency() {
        Set<PrivilegeTypeConstant> missingPrivileges = getMissingPrivileges();

        if (missingPrivileges.isEmpty()) {
            log.debug("All privilege enum constants have corresponding database entities");
            return true;
        } else {
            log.warn("Found {} missing privileges in database: {}",
                    missingPrivileges.size(), missingPrivileges);
            return false;
        }
    }

    @Override
    public Set<PrivilegeTypeConstant> getMissingPrivileges() {
        // Get all enum constants
        Set<PrivilegeTypeConstant> allEnumConstants = Arrays.stream(PrivilegeTypeConstant.values())
                .collect(Collectors.toSet());

        // Get all privileges from database
        List<PrivilegeEntity> dbPrivileges = privilegeEntityDao.getRecords();
        Set<PrivilegeTypeConstant> dbPrivilegeTypes = dbPrivileges.stream()
                .map(PrivilegeEntity::getType)
                .collect(Collectors.toSet());

        // Find missing ones
        allEnumConstants.removeAll(dbPrivilegeTypes);
        return allEnumConstants;
    }

    @Override
    public Set<PrivilegeTypeConstant> getOrphanedPrivileges() {
        // Get all enum constants
        Set<PrivilegeTypeConstant> allEnumConstants = Arrays.stream(PrivilegeTypeConstant.values())
                .collect(Collectors.toSet());

        // Get all privileges from database
        List<PrivilegeEntity> dbPrivileges = privilegeEntityDao.getRecords();
        Set<PrivilegeTypeConstant> dbPrivilegeTypes = dbPrivileges.stream()
                .map(PrivilegeEntity::getType)
                .collect(Collectors.toSet());

        // Find orphaned ones (in DB but not in enum)
        dbPrivilegeTypes.removeAll(allEnumConstants);
        return dbPrivilegeTypes;
    }

    @Override
    public int synchronizePrivileges(boolean removeOrphaned) {
        int changesCount = 0;

        // Create missing privileges
        Set<PrivilegeTypeConstant> missingPrivileges = getMissingPrivileges();
        for (PrivilegeTypeConstant privilegeType : missingPrivileges) {
            createPrivilegeEntity(privilegeType);
            changesCount++;
            log.info("Created missing privilege: {}", privilegeType.name());
        }

        // Optionally remove orphaned privileges
        if (removeOrphaned) {
            Set<PrivilegeTypeConstant> orphanedPrivileges = getOrphanedPrivileges();
            for (PrivilegeTypeConstant privilegeType : orphanedPrivileges) {
                privilegeEntityDao.findByType(privilegeType)
                        .ifPresent(privilege -> {
                            privilege.setRecordStatus(RecordStatusConstant.DELETED);
                            privilegeEntityDao.saveRecord(privilege);
                            log.info("Marked orphaned privilege as deleted: {}", privilegeType.name());
                        });
                changesCount++;
            }
        }

        log.info("Privilege synchronization completed. Changes made: {}", changesCount);
        return changesCount;
    }

    private void createPrivilegeEntity(PrivilegeTypeConstant privilegeType) {
        PrivilegeEntity privilegeEntity = new PrivilegeEntity();
        privilegeEntity.setType(privilegeType);
        privilegeEntity.setRecordStatus(RecordStatusConstant.ACTIVE);

        privilegeEntityDao.saveRecord(privilegeEntity);
    }
}
