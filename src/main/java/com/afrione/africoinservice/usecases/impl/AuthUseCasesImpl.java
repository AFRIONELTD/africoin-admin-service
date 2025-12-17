package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.dao.SessionDataEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.domain.entities.SessionDataEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.domain.entities.enums.SessionDataTypeConstant;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.usecases.AuthUseCases;
import com.afrione.africoinservice.usecases.data.request.LoginPasswordSD;
import com.afrione.africoinservice.usecases.data.request.LoginRequest;
import com.afrione.africoinservice.usecases.data.request.ChangePasswordRequest;
import com.afrione.africoinservice.usecases.data.response.auth.Toggle2FAResponse;
import com.afrione.africoinservice.usecases.data.response.login.LoginResponse;
import com.afrione.africoinservice.usecases.data.value_objects.AppConstant;
import com.afrione.africoinservice.usecases.data.value_objects.AppToken;
import com.afrione.africoinservice.usecases.data.value_objects.CustomerToken;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.usecases.exceptions.UnauthorisedAccessException;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;
import com.afrione.africoinservice.usecases.data.request.Toggle2FARequest;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
    private final SessionDataEntityDao sessionDataEntityDao;
    private final Gson gson;

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    @Override
    public String login(LoginRequest request) {
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

            LoginPasswordSD loginPasswordSD = new LoginPasswordSD();
            loginPasswordSD.setUserId(user.getId());
            loginPasswordSD.setEncryptedToken(passwordEncoder.encode("123456")); //to be changed

            SessionDataEntity sessionDataEntity = new SessionDataEntity();
            sessionDataEntity.setSessionId(sessionDataEntityDao.generateSessionId());
            sessionDataEntity.setSessionDataType(SessionDataTypeConstant.WEB_LOGIN.name());
            sessionDataEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
            sessionDataEntity.setPayload(gson.toJson(loginPasswordSD));
            sessionDataEntityDao.saveRecord(sessionDataEntity);
            return sessionDataEntity.getSessionId();

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

    @Override
    public LoginResponse completeLogin(String sessionId, String token) {

        SessionDataEntity sd = sessionDataEntityDao
                .findBySessionIdAndType(sessionId, SessionDataTypeConstant.WEB_LOGIN)
                .orElseThrow(() -> new BadRequestException("Invalid or expired session"));

        LoginPasswordSD loginPasswordSD = null;
        AppUserEntity user = null;

        try {
            if (sd.getExpiryTime().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Session has expired");
            }

            loginPasswordSD = gson.fromJson(sd.getPayload(), LoginPasswordSD.class);

            if(loginPasswordSD.isVerified()){
                throw new BadRequestException("Token has already been used");
            }

            if (loginPasswordSD.getTokenTrial() >= MAX_FAILED_LOGIN_ATTEMPTS) {
                sd.setRecordStatus(RecordStatusConstant.DELETED);
                throw new BadRequestException("Maximum token attempts exceeded");
            }

            if (!passwordEncoder.matches(token, loginPasswordSD.getEncryptedToken())) {
                loginPasswordSD.setTokenTrial(loginPasswordSD.getTokenTrial() + 1);
                throw new BadRequestException("Invalid token");
            }

            loginPasswordSD.setVerified(true);

            user = appUserEntityDao.findById(loginPasswordSD.getUserId())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            user.setFailedLoginAttempts(0);
            user.setLastLoginAt(OffsetDateTime.now());

            Map<String, String> tokenAttributes = new HashMap<>();
            tokenAttributes.put("userId", user.getId().toString());
            tokenAttributes.put("email", user.getEmail());
            tokenAttributes.put("authKey", user.getAuthenticationKey());
            tokenAttributes.put("accountType", AppConstant.ACCOUNT_TYPE_ADMIN);
            tokenAttributes.put(AppConstant.TOKEN_TYPE, AppConstant.TOKEN_TYPE_ACCESS);
            // Additional claims to include in the token
            // roles as CSV
            String rolesCsv = user.getRoles().stream().map(RoleEntity::getRoleName).collect(Collectors.joining(","));
            tokenAttributes.put("roles", rolesCsv);
            // privileges aggregated from roles as CSV (distinct)
            String privilegesCsv = user.getRoles().stream()
                    .flatMap(r -> r.getPrivileges().stream())
                    .map(p -> p.getType().name())
                    .distinct()
                    .collect(Collectors.joining(","));
            tokenAttributes.put("privileges", privilegesCsv);
            // flags and metadata
            tokenAttributes.put("requiresPasswordChange", String.valueOf(user.isRequiresPasswordChange()));
            tokenAttributes.put("twoFAEnabled", String.valueOf(user.isTwoFAEnabled()));
            tokenAttributes.put("twoFAMethod", user.getTwoFAMethod() == null ? "" : user.getTwoFAMethod());
            tokenAttributes.put("lastLoginAt", user.getLastLoginAt() == null ? "" : user.getLastLoginAt().toString());
            // token identifiers
            tokenAttributes.put("jti", UUID.randomUUID().toString());
            tokenAttributes.put("iat", String.valueOf(Instant.now().getEpochSecond()));

            int accessTokenExpiryTime =
                    applicationProperty.getAccessTokenExpiryTimeInMinutes();
            int refreshTokenExpiryTime =
                    applicationProperty.getRefreshTokenExpiryTimeInMinutes();

            String accessTokenString = jwtService.expiringToken(
                    applicationProperty.getClientTokenSecretKey(),
                    tokenAttributes,
                    accessTokenExpiryTime
            );

            tokenAttributes.put(AppConstant.TOKEN_TYPE, AppConstant.TOKEN_TYPE_REFRESH);

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

            PortalUserModel portalUserModel = PortalUserModel.builder()
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .email(user.getEmail())
                    .requirePasswordChange(user.isRequiresPasswordChange())
                    .roles(
                            user.getRoles().stream()
                                    .map(RoleEntity::getRoleName)
                                    .collect(Collectors.toList())
                    )
                    .build();

            LoginResponse response = new LoginResponse();

            if (!user.isRequiresPasswordChange()) {
                response.setUserToken(customerToken);
            }

            response.setUser(portalUserModel);

            log.info("User logged in successfully: {}", user.getEmail());

            return response;

        } finally {

            if (loginPasswordSD != null) {
                sd.setPayload(gson.toJson(loginPasswordSD));
            }

            sessionDataEntityDao.saveRecord(sd);

            if (user != null) {
                appUserEntityDao.saveRecord(user);
            }
        }
    }

}
