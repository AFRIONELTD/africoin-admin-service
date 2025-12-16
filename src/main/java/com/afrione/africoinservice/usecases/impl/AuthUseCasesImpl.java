package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.usecases.AuthUseCases;
import com.afrione.africoinservice.usecases.data.request.LoginRequest;
import com.afrione.africoinservice.usecases.data.request.ChangePasswordRequest;
import com.afrione.africoinservice.usecases.data.response.auth.Toggle2FAResponse;
import com.afrione.africoinservice.usecases.data.response.login.LoginResponse;
import com.afrione.africoinservice.usecases.data.value_objects.AppToken;
import com.afrione.africoinservice.usecases.data.value_objects.CustomerToken;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.usecases.exceptions.UnauthorisedAccessException;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;
import com.afrione.africoinservice.usecases.data.request.Toggle2FARequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by felixadewale on
 * 08/12/2025
 */

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthUseCasesImpl implements AuthUseCases {
    private final AppUserEntityDao appUserEntityDao;
    private final JWTService jwtService;
    private final ApplicationProperty applicationProperty;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    @Override
    public LoginResponse login(LoginRequest request) {
        AppUserEntity user = appUserEntityDao.findRecordByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login attempt with non-existent email: {}", request.getEmail());
                    return new UnauthorisedAccessException("Invalid email or password");
                });

        try {
            if (user.getFailedLoginAttempts() >= MAX_FAILED_LOGIN_ATTEMPTS) {
                log.warn("Login attempt for locked account (too many failed attempts): {}", request.getEmail());
                user.setRecordStatus(RecordStatusConstant.INACTIVE);
                throw new BadRequestException("Account is locked due to too many failed login attempts. Please contact support.");
            }

            if (user.getRecordStatus() != RecordStatusConstant.ACTIVE) {
                log.warn("Login attempt for inactive user: {}", request.getEmail());
                throw new BadRequestException("User account is not active");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

                int remainingAttempts = MAX_FAILED_LOGIN_ATTEMPTS - user.getFailedLoginAttempts();
                log.warn("Failed login attempt for user: {}. Remaining attempts: {}", request.getEmail(), remainingAttempts);

                throw new UnauthorisedAccessException("Invalid email or password. Remaining attempts: " + remainingAttempts);
            }

            user.setFailedLoginAttempts(0);
            user.setLastLoginAt(OffsetDateTime.now());

            Map<String, String> tokenAttributes = new HashMap<>();
            tokenAttributes.put("userId", user.getId().toString());
            tokenAttributes.put("email", user.getEmail());

            int accessTokenExpiryTime = applicationProperty.getAccessTokenExpiryTimeInMinutes();
            int refreshTokenExpiryTime = applicationProperty.getRefreshTokenExpiryTimeInMinutes();

            String accessTokenString = jwtService.expiringToken(
                    applicationProperty.getClientTokenSecretKey(),
                    tokenAttributes,
                    accessTokenExpiryTime
            );

            String refreshTokenString = jwtService.expiringToken(
                    applicationProperty.getClientTokenSecretKey(),
                    tokenAttributes,
                    refreshTokenExpiryTime
            );

            AppToken accessToken = AppToken.builder()
                    .token(accessTokenString)
                    .expiryTimeInMinutes(accessTokenExpiryTime)
                    .build();

            AppToken refreshToken = AppToken.builder()
                    .token(refreshTokenString)
                    .expiryTimeInMinutes(refreshTokenExpiryTime)
                    .build();

            CustomerToken customerToken = CustomerToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            String fullName = user.getFirstName() + " " + user.getLastName();
            java.util.List<String> roles = user.getRoles().stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.toList());

            PortalUserModel portalUserModel = PortalUserModel.builder()
                    .fullName(fullName)
                    .email(user.getEmail())
                    .requirePasswordChange(user.isRequiresPasswordChange())
                    .roles(roles)
                    .build();

            LoginResponse response = new LoginResponse();
            if (!user.isRequiresPasswordChange()) {
                response.setUserToken(customerToken);
            }
            response.setUser(portalUserModel);

            log.info("User logged in successfully: {}", request.getEmail());
            return response;
        } finally {
            appUserEntityDao.saveRecord(user);
        }
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Long userId) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirmation password do not match");
        }

        AppUserEntity user = appUserEntityDao.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Change password attempt for non-existent user: {}", userId);
                    return new UnauthorisedAccessException("User not found");
                });

        try {
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                log.warn("Failed password change attempt for user: {}", userId);
                throw new UnauthorisedAccessException("Current password is incorrect");
            }

            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new BadRequestException("New password cannot be the same as current password");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setRequiresPasswordChange(false);

            log.info("Password changed successfully for user: {}", userId);
        } finally {
            appUserEntityDao.saveRecord(user);
        }
    }

    @Override
    public Toggle2FAResponse enable2FA(Toggle2FARequest request, Long userId) {
        String method = request.getTwoFAMethod().toUpperCase();
        if (!method.matches("SMS|EMAIL|AUTHENTICATOR")) {
            throw new BadRequestException("Invalid 2FA method. Must be SMS, EMAIL, or AUTHENTICATOR");
        }

        AppUserEntity user = appUserEntityDao.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Enable 2FA attempt for non-existent user: {}", userId);
                    return new UnauthorisedAccessException("User not found");
                });

        try {
            user.setTwoFAEnabled(true);
            user.setTwoFAMethod(method);
            log.info("2FA enabled for user: {} with method: {}", userId, method);
        } finally {
            appUserEntityDao.saveRecord(user);
        }

        return Toggle2FAResponse.builder()
                .twoFAEnabled(true)
                .twoFAMethod(method)
                .message("2FA enabled successfully with method: " + method)
                .build();
    }

    @Override
    public Toggle2FAResponse disable2FA(Long userId) {
        AppUserEntity user = appUserEntityDao.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Disable 2FA attempt for non-existent user: {}", userId);
                    return new UnauthorisedAccessException("User not found");
                });

        try {
            user.setTwoFAEnabled(false);
            user.setTwoFAMethod(null);
            log.info("2FA disabled for user: {}", userId);
        } finally {
            appUserEntityDao.saveRecord(user);
        }

        return Toggle2FAResponse.builder()
                .twoFAEnabled(false)
                .twoFAMethod(null)
                .message("2FA disabled successfully")
                .build();
    }
}
