package com.afrione.africoinservice.infrastructure.configs;

import com.afrione.africoinservice.domain.services.PrivilegeValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Initializes privilege entities for all enum constants at startup.
 * Ensures database consistency with enum definitions.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1) // Run before AdminInitializer
public class PrivilegeInitializer implements CommandLineRunner {

    private final PrivilegeValidationService privilegeValidationService;

    @Override
    public void run(String... args) {
        try {
            log.info("Starting privilege initialization...");

            // Synchronize privileges (create missing ones, don't remove orphaned)
            int changesCount = privilegeValidationService.synchronizePrivileges(false);

            if (changesCount > 0) {
                log.info("Privilege initialization completed. {} privileges were created.", changesCount);
            } else {
                log.info("Privilege initialization completed. All privileges already exist.");
            }

            // Validate consistency
            if (privilegeValidationService.validatePrivilegeConsistency()) {
                log.info("Privilege consistency validation passed.");
            } else {
                log.warn("Privilege consistency validation failed. Some enum constants may not have database entities.");
            }

        } catch (Exception e) {
            log.error("Error during privilege initialization", e);
            throw new RuntimeException("Privilege initialization failed", e);
        }
    }
}
