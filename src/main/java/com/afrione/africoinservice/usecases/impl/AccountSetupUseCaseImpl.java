package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.dao.RoleEntityDao;
import com.afrione.africoinservice.domain.dao.SessionDataEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.domain.entities.SessionDataEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.domain.entities.enums.SessionDataTypeConstant;
import com.afrione.africoinservice.domain.services.EmailService;
import com.afrione.africoinservice.domain.services.SequenceGenerator;
import com.afrione.africoinservice.usecases.AccountSetupUseCases;
import com.afrione.africoinservice.usecases.data.request.AccountSetupRequest;
import com.afrione.africoinservice.usecases.data.request.ForgotPasswordSD;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.account_setup.AccountSetupResponse;
import com.afrione.africoinservice.usecases.data.response.auth.ForgotPasswordResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;
import com.afrione.africoinservice.utils.RandomPasswordGenerator;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Created by felixadewale on
 * 08/12/2025
 */

@RequiredArgsConstructor
@Slf4j
@Component
@Transactional
public class AccountSetupUseCaseImpl implements AccountSetupUseCases {

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;


    private final AppUserEntityDao appUserEntityDao;
    private final Random random = new Random();
    private final PasswordEncoder passwordEncoder;
    private final RoleEntityDao rolesDao;
    private final SessionDataEntityDao sessionDataEntityDao;
    private final SequenceGenerator sequenceGenerator;
    private final EmailService emailService;
    private final Gson gson = new Gson();

    @Override
    public AccountSetupResponse setupAccount(AccountSetupRequest request, Long userId) {
        boolean existWithEmail = appUserEntityDao.recordExistWithEmail(request.getEmail());
        if (existWithEmail) {
            throw new BadRequestException("Email already in use");
        }
        Optional<AppUserEntity> optionalAppUser = appUserEntityDao.findById(userId);
        if (optionalAppUser.isEmpty()) {
            throw new BadRequestException("Creator not found");
        }
        AppUserEntity creator = optionalAppUser.get();
        String password = RandomPasswordGenerator.randomPasswordGenerator(random.nextInt(8, 20));
        AppUserEntity appUserEntity = AppUserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .createdBy(creator)
                .authenticationKey("admin-auth-key-" + System.currentTimeMillis())
                .password(passwordEncoder.encode(password))
                .build();

        List<RoleEntity> roleEntityList = request.getRoles().parallelStream().map(role -> rolesDao.findById(role).orElseThrow(() -> new BadRequestException("Role " + role + " not found"))).collect(Collectors.toList());

        appUserEntity.setRoles(roleEntityList);

        appUserEntityDao.saveRecord(appUserEntity);

        String message = String.format("You have been invited to Afrione backoffice \n email : %s  \n  password : %s \n you can change your password when you are ready" , request.getEmail(), password);

        emailService.sendNotice(request.getEmail(), message, "BACKOFFICE INVITATION" );

        return AccountSetupResponse
                .builder()
                .firstName(appUserEntity.getFirstName())
                .lastName(appUserEntity.getLastName())
                .gender(appUserEntity.getGender())
                .email(appUserEntity.getEmail())
                .phoneNumber(appUserEntity.getPhoneNumber())
                .defaultPassword(password)
                .build();
    }

    @Override
    public ForgotPasswordResponse initiateForgotPassword(String emailAddress) {
        AppUserEntity appUserEntity = appUserEntityDao.findRecordByEmail(emailAddress).orElseThrow(() -> new BadRequestException("User with email address not found"));
        int exp = 300;
        SessionDataEntity sessionDataEntity = new SessionDataEntity();
        sessionDataEntity.setSessionId(sessionDataEntityDao.generateSessionId());
        sessionDataEntity.setSessionDataType(SessionDataTypeConstant.FORGOT_PASSWORD.name());
        sessionDataEntity.setExpiryTime(LocalDateTime.now().plusSeconds(exp));
        String generatedCode = sequenceGenerator.generateCode(6);

        log.info("forgot password otp: {}", generatedCode);

        ForgotPasswordSD forgotPasswordSD = new ForgotPasswordSD();
        forgotPasswordSD.setEncryptedToken(passwordEncoder.encode(generatedCode));
        forgotPasswordSD.setUserId(appUserEntity.getId());
        forgotPasswordSD.setExpiryInSeconds(exp);
        forgotPasswordSD.setEmail(emailAddress);
        sessionDataEntity.setPayload(gson.toJson(forgotPasswordSD));
        sessionDataEntityDao.saveRecord(sessionDataEntity);
        //publish the generated code to user's email address

        String message = "Otp code :" + generatedCode;

        emailService.sendNotice(emailAddress, message, "OTP CODE FOR PASSWORD CHANGE");

        return new ForgotPasswordResponse(sessionDataEntity.getSessionId(), exp);
    }

    @Override
    public void finaliseForgotPassword(String sessionId, String otp, String newPassword) {
        sessionDataEntityDao.findBySessionIdAndType(sessionId, SessionDataTypeConstant.FORGOT_PASSWORD).ifPresentOrElse(sessionDataEntity -> {
            ForgotPasswordSD forgotPasswordSD = gson.fromJson(sessionDataEntity.getPayload(), ForgotPasswordSD.class);

            if (forgotPasswordSD.isVerified()) {
                throw new BadRequestException("Token has already been used");
            }

            if (forgotPasswordSD.getTokenTrial() >= MAX_FAILED_LOGIN_ATTEMPTS) {
                sessionDataEntity.setRecordStatus(RecordStatusConstant.DELETED);
                throw new BadRequestException("Maximum token attempts exceeded");
            }

            if (!passwordEncoder.matches(otp, forgotPasswordSD.getEncryptedToken())) {
                forgotPasswordSD.setTokenTrial(forgotPasswordSD.getTokenTrial() + 1);
                throw new BadRequestException("Invalid token");
            }
            if (forgotPasswordSD.getExpiryInSeconds() <= 0 || sessionDataEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Session has expired");
            }

            forgotPasswordSD.setVerified(true);
            sessionDataEntity.setPayload(gson.toJson(forgotPasswordSD));
            sessionDataEntityDao.saveRecord(sessionDataEntity);
            AppUserEntity appUserEntity = appUserEntityDao.getRecordById(forgotPasswordSD.getUserId());
            appUserEntity.setRequiresPasswordChange(false);
            appUserEntity.setPassword(passwordEncoder.encode(newPassword));
            appUserEntityDao.saveRecord(appUserEntity);
        }, () -> {
            throw new BadRequestException("Invalid session ID");
        });
    }

    @Override
    public ForgotPasswordResponse resendToken(String sessionId) {
        SessionDataEntity sessionDataEntity = sessionDataEntityDao.findBySessionIdAndType(sessionId, SessionDataTypeConstant.FORGOT_PASSWORD).orElseThrow(() -> new BadRequestException("Invalid session ID"));
        sessionDataEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        ForgotPasswordSD forgotPasswordSD = gson.fromJson(sessionDataEntity.getPayload(), ForgotPasswordSD.class);
        String generatedCode = sequenceGenerator.generateCode(6);
        forgotPasswordSD.setEncryptedToken(passwordEncoder.encode(generatedCode));
        sessionDataEntity.setPayload(gson.toJson(forgotPasswordSD));
        sessionDataEntityDao.saveRecord(sessionDataEntity);

        String message = "Otp code :" + generatedCode;

        emailService.sendNotice(forgotPasswordSD.getEmail(), message, "OTP CODE FOR PASSWORD CHANGE");

        return new ForgotPasswordResponse(sessionDataEntity.getSessionId(), Math.abs((int) Duration.between(LocalDateTime.now(), sessionDataEntity.getExpiryTime()).toSeconds()));
    }

    @Override
    public void deleteUser(Long userId, Long authenticatedUserId) {
        AppUserEntity appUserEntity = appUserEntityDao.getRecordById(userId);
        appUserEntity.setRecordStatus(RecordStatusConstant.DELETED);
        appUserEntityDao.saveRecord(appUserEntity);
    }

    @Override
    public PagedResponse<PortalUserModel> getAllUsers(int pageNo, int pageSize, String searchTerm, Long roleId, Long accountId) {
        Page<AppUserEntity> userEntityPage = appUserEntityDao.findAllUsers(pageNo, pageSize, searchTerm, roleId);
        return new PagedResponse<>(userEntityPage.getTotalElements(), userEntityPage.getTotalPages(),
                userEntityPage.getContent().stream().map(PortalUserModel::toModel).toList());
    }

    @Override
    public void updateAccount(Long userId, AccountSetupRequest request, Long userId1) {
        AppUserEntity appUserEntity = appUserEntityDao.getRecordById(userId);
        if (StringUtils.isNotBlank(request.getFirstName())) {
            appUserEntity.setFirstName(request.getFirstName());
        }

        if (StringUtils.isNotBlank(request.getLastName())) {
            appUserEntity.setLastName(request.getLastName());
        }

        if (StringUtils.isNotBlank(request.getPhoneNumber())) {
            appUserEntity.setPhoneNumber(request.getPhoneNumber());
        }

        if (Objects.nonNull(request.getEmail()) && appUserEntity.isRequiresPasswordChange()) {
            appUserEntity.setEmail(request.getEmail());
        }

        if (Objects.nonNull(request.getRoles()) && !request.getRoles().isEmpty()) {
            List<RoleEntity> roleEntityList = request.getRoles().parallelStream().map(role -> rolesDao.findById(role).orElseThrow(() -> new BadRequestException("Role " + role + " not found"))).collect(Collectors.toList());
            appUserEntity.setRoles(roleEntityList);
        }

        appUserEntityDao.saveRecord(appUserEntity);

    }

}
