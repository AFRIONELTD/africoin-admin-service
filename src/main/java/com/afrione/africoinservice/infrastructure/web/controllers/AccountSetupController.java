package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.AccountSetupUseCases;
import com.afrione.africoinservice.usecases.data.request.AccountSetupRequest;
import com.afrione.africoinservice.usecases.data.response.account_setup.AccountSetupResponse;
import com.afrione.africoinservice.usecases.data.response.auth.ForgotPasswordResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by felixadewale on
 * 08/12/2025
 */
@Transactional
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/account-setup/", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Validated
public class AccountSetupController {

    private final AccountSetupUseCases accountSetupUseCases;

    @PostMapping(value = "setup-account", headers = {"Authorization"})
    public ApiResponseJSON<AccountSetupResponse> setupAccount(@RequestBody @Valid AccountSetupRequestJSON request, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        AccountSetupResponse accountSetupResponse = accountSetupUseCases.setupAccount(request.toRequest(), authenticatedUser.getUserId());
        return new ApiResponseJSON<>("Account setup successful", accountSetupResponse);
    }

    @DeleteMapping(value = "{userId}", headers = {"Authorization"})
    public ApiResponseJSON<AccountSetupResponse> setupAccount(@PathVariable Long userId, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        accountSetupUseCases.deleteUser(userId, authenticatedUser.getUserId());
        return new ApiResponseJSON<>("User deleted successfully");
    }

    @PostMapping("forgot-password/initiate/{emailAddress}")
    public ApiResponseJSON<ForgotPasswordResponse> initiateForgotPassword(@PathVariable @Email String emailAddress) {
        ForgotPasswordResponse response = accountSetupUseCases.initiateForgotPassword(emailAddress);
        return new ApiResponseJSON<>("Forgot password initiated successfully", response);
    }

    @PostMapping("forgot-password/finalize/{sessionId}")
    public ApiResponseJSON<ForgotPasswordResponse> finaliseForgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest, @PathVariable String sessionId) {
        accountSetupUseCases.finaliseForgotPassword(sessionId, forgotPasswordRequest.otp, forgotPasswordRequest.newPassword);
        return new ApiResponseJSON<>("Password changed successfully");
    }

    @PostMapping("forgot-password/resend-token/{sessionId}")
    public ApiResponseJSON<ForgotPasswordResponse> resendToken(@PathVariable String sessionId) {
        ForgotPasswordResponse response =  accountSetupUseCases.resendToken(sessionId);
        return new ApiResponseJSON<>("Token resent successfully", response);
    }


    @Data
    public static class AccountSetupRequestJSON {
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @NotBlank
        @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Invalid Gender")
        private String gender;
        @NotBlank
        private String phoneNumber;
        @NotBlank
        @Email
        private String email;
        private Set<@Positive Long> roles;

        public AccountSetupRequest toRequest() {
            return AccountSetupRequest.builder()
                    .firstName(this.firstName)
                    .lastName(this.lastName)
                    .email(this.email)
                    .gender(this.gender)
                    .phoneNumber(this.phoneNumber)
                    .roles(this.roles)
                    .build();
        }
    }

    @Data
    public static class ForgotPasswordRequest {

        @NotBlank(message = "New password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
        )
        private String newPassword;

        @NotBlank(message = "OTP is required")
        private String otp;
    }

}
