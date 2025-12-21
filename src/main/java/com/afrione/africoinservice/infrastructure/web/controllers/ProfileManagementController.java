package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.ProfileManagementUseCases;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by felixadewale on
 * 08/12/2025
 */
@Transactional
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/profile/", produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Authorization"})
@RestController
@Validated
public class ProfileManagementController {

    private final ProfileManagementUseCases profileManagementUseCases;

    @GetMapping("user")
    public ApiResponseJSON<PortalUserModel> getUser(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        PortalUserModel userModel = profileManagementUseCases.getUser(authenticatedUser.getUserId());
        return new ApiResponseJSON<>("Profile retrieved successfully", userModel);
    }
}
