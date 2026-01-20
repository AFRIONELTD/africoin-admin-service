package com.afrione.africoinservice.domain.services;

import com.afrione.africoinservice.domain.entities.enums.PrivilegeTypeConstant;

import java.util.List;
import java.util.Set;

/**
 * Service for managing privilege validation and consistency checks
 */
public interface PrivilegeValidationService {

    /**
     * Validates that all enum constants have corresponding database entities
     * @return true if all enum constants are present in database
     */
    boolean validatePrivilegeConsistency();

    /**
     * Gets enum constants that are missing from database
     * @return set of missing privilege types
     */
    Set<PrivilegeTypeConstant> getMissingPrivileges();

    /**
     * Gets database privileges that don't have corresponding enum constants
     * @return set of orphaned privilege types
     */
    Set<PrivilegeTypeConstant> getOrphanedPrivileges();

    /**
     * Synchronizes database with enum constants
     * Creates missing privileges and optionally removes orphaned ones
     * @param removeOrphaned whether to remove privileges not in enum
     * @return number of changes made
     */
    int synchronizePrivileges(boolean removeOrphaned);
}
