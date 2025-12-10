package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.dao.RoleEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.usecases.AccountSetupUseCases;
import com.afrione.africoinservice.usecases.data.request.AccountSetupRequest;
import com.afrione.africoinservice.usecases.data.response.account_setup.AccountSetupResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.RandomPasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
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
public class AccountSetupUseCaseImpl implements AccountSetupUseCases {

    private final AppUserEntityDao appUserEntityDao;
    private final Random random = new Random();
    private final PasswordEncoder passwordEncoder;
    private final RoleEntityDao rolesDao;

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
                .phoneNumber(request.getPhoneNumber())
                .createdBy(creator)
                .password(passwordEncoder.encode(password))
                .build();

        List<RoleEntity> roleEntityList = request.getRoles().parallelStream().map(role -> rolesDao.findByName(role).orElseThrow(() -> new BadRequestException("Role " + role + " not found"))).collect(Collectors.toList());

        appUserEntity.setRoles(roleEntityList);

        appUserEntityDao.saveRecord(appUserEntity);

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

}
