package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.AccountSetupUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.account_setup.AccountSetupResponse;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by felixadewale on
 * 08/12/2025
 */
@Transactional
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/account", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Validated
public class AccountController {

    private final AccountSetupUseCases accountSetupUseCases;



    @DeleteMapping(value = "{userId}", headers = {"Authorization"})
    public ApiResponseJSON<AccountSetupResponse> deleteAccount(@PathVariable Long userId, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        accountSetupUseCases.deleteUser(userId, authenticatedUser.getUserId());
        return new ApiResponseJSON<>("User deleted successfully");
    }

    @GetMapping
    public ApiResponseJSON<PagedResponse<PortalUserModel>> getAllUsers(@RequestParam(defaultValue = "0") @PositiveOrZero int pageNo,
                                                              @RequestParam(defaultValue = "10") @Max(value = 50, message = "Max page size is 50") @Positive int pageSize, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        PagedResponse<PortalUserModel> users = accountSetupUseCases.getAllUsers(pageNo, pageSize, authenticatedUser.getAccountId());
        return new ApiResponseJSON<>("Users fetched successfully", users);
    }


}
