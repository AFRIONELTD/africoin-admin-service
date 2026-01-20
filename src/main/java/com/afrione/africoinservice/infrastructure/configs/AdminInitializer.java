package com.afrione.africoinservice.infrastructure.configs;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.dao.PrivilegeEntityDao;
import com.afrione.africoinservice.domain.dao.RoleEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.PrivilegeEntity;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.EnvironmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Initializes an admin user at startup if none exists.
 * Created by felixadewale on 10/12/2025
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after PrivilegeInitializer
public class AdminInitializer implements CommandLineRunner {

    private final AppUserEntityDao appUserEntityDao;
    private final RoleEntityDao roleEntityDao;
    private final PrivilegeEntityDao privilegeEntityDao;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationProperty applicationProperty;


    @Override
    public void run(String... args) {
        try {
            System.out.println(applicationProperty.getMerchantTokenSecretKey());
            System.out.println(applicationProperty.geCustomerTokenSecretKey());
            if (appUserEntityDao.findRecordByEmail("admin@africoin.com").isEmpty()) {
                log.info("No admin user found. Creating default admin user...");
                createDefaultAdmin();
                log.info("Default admin user created successfully.");
            } else {
                log.info("Admin user already exists. Skipping initialization.");
            }
        } catch (Exception e) {
            log.error("Error during admin initialization", e);
        }
    }

    private void createDefaultAdmin() {
        try {
            // Get or create ADMIN role
            RoleEntity adminRole = roleEntityDao.findByName("ADMIN")
                    .orElseGet(() -> createAdminRole());

            // Create admin user
            AppUserEntity adminUser = AppUserEntity.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@africoin.com")
                    .phoneNumber("+1234567890")
                    .password(passwordEncoder.encode("Admin@123456"))
                    .authenticationKey("admin-auth-key-" + System.currentTimeMillis())
                    .requiresPasswordChange(true)
                    .roles(Collections.singletonList(adminRole))
                    .failedLoginAttempts(0)
                    .twoFAEnabled(false)
                    .build();

            appUserEntityDao.saveRecord(adminUser);
            log.info("Created default admin user with email: admin@africoin.com");
            log.info("Default password: Admin@123456 (CHANGE THIS IN PRODUCTION)"); //TODO
        } catch (Exception e) {
            log.error("Failed to create default admin user", e);
            throw new RuntimeException("Admin initialization failed", e);
        }
    }

    private RoleEntity createAdminRole() {
        // Get all privileges for the admin role
        List<PrivilegeEntity> allPrivileges = privilegeEntityDao.getRecords();

        RoleEntity adminRole = new RoleEntity();
        adminRole.setRoleName("SUPER ADMIN");
        adminRole.setPrivileges(allPrivileges); // Admin gets all privileges
        adminRole.setRecordStatus(RecordStatusConstant.ACTIVE);

        RoleEntity savedRole = roleEntityDao.saveRecord(adminRole);
        log.info("Created ADMIN role with {} privileges", allPrivileges.size());
        return savedRole;
    }
}

