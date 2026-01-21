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
        return PortalUserModel.toModel(appUserEntity);
    }
}
