package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;

/**
 * Created by felixadewale on
 * 21/12/2025
 */
public interface ProfileManagementUseCases {
    PortalUserModel getUser(Long userId);
}
