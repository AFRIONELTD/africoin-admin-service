package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.usecases.ProfileManagementUseCases;
import com.afrione.africoinservice.usecases.exceptions.UnauthorisedAccessException;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by felixadewale on
 * 21/12/2025
 */

@Component
@RequiredArgsConstructor
public class ProfileManagementUseCasesImpl implements ProfileManagementUseCases {

    private final AppUserEntityDao appUserEntityDao;

    @Override
    public PortalUserModel getUser(Long userId) {
        var appUserEntity = appUserEntityDao.findById(userId).orElseThrow(() -> new UnauthorisedAccessException("User not found"));
        return PortalUserModel
                .builder()
                .fullName(appUserEntity.getFirstName() + " " + appUserEntity.getLastName())
                .email(appUserEntity.getEmail())
                .twoFAEnabled(appUserEntity.isTwoFAEnabled())
                .twoFAMethod(appUserEntity.getTwoFAMethod() != null ? appUserEntity.getTwoFAMethod() : null)
                .requirePasswordChange(appUserEntity.isRequiresPasswordChange())
                .roles(appUserEntity.getRoles().stream().map(RoleEntity::getRoleName).toList())
                .dateJoined(appUserEntity.getDateCreated().toLocalDate())
                .lastLoginDate(appUserEntity.getLastLoginAt() == null ? null : appUserEntity.getLastLoginAt().toLocalDate())
                .phoneNumber(appUserEntity.getPhoneNumber())
                .build();
    }
}
