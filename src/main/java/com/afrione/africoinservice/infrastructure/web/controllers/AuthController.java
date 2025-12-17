package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.AuthUseCases;
import com.afrione.africoinservice.usecases.data.request.LoginRequest;
import com.afrione.africoinservice.usecases.data.request.ChangePasswordRequest;
import com.afrione.africoinservice.usecases.data.request.Toggle2FARequest;
import com.afrione.africoinservice.usecases.data.response.login.LoginResponse;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by felixadewale on
 * 08/12/2025
 */
@Transactional(dontRollbackOn = Exception.class)
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Validated
public class AuthController {

    private final AuthUseCases authUseCases;

    @PostMapping("login")
    public ApiResponseJSON<String> login(@RequestBody @Valid LoginRequestJSON request) {
        String sessionId = authUseCases.login(request.toRequest());
        return new ApiResponseJSON<>("Login initiated successfully. Input your 2FA token to continue", sessionId);
    }

    @PostMapping("login/2fa")
    public ApiResponseJSON<LoginResponse> login(@RequestHeader String sessionId, @RequestBody @Valid Token2FaRequestJSON requestJSON) {
        LoginResponse response = authUseCases.completeLogin(sessionId, requestJSON.token);
        PortalUserModel user = response.getUser();
        return new ApiResponseJSON<>(user.isRequirePasswordChange() ? "Password change required! Please reset password ":"Login successful", response);
    }

    @PostMapping("login/change-password")
    public ApiResponseJSON<LoginResponse> login(@RequestHeader String sessionId, @RequestBody @Valid ChangePasswordOnLoginRequestJSON requestJSON) {
        LoginResponse response = authUseCases.changePasswordOnLogin(sessionId, requestJSON.newPassword);
        return new ApiResponseJSON<>("Password change successful", response);
    }

    @PostMapping("change-password")
    public ApiResponseJSON<String> changePassword(@RequestBody @Valid ChangePasswordRequestJSON request, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        authUseCases.changePassword(request.toRequest(), authenticatedUser.getUserId());
        return new ApiResponseJSON<>("Password changed successfully", "Password updated");
    }

//    @PostMapping("2fa/enable")
//    public ApiResponseJSON<Toggle2FAResponse> enable2FA(@RequestBody @Valid Toggle2FARequestJSON request, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
//        Toggle2FAResponse response = authUseCases.enable2FA(request.toRequest(), authenticatedUser.getUserId());
//        return new ApiResponseJSON<>("2FA enabled successfully", response);
//    }
//
//    @PostMapping("2fa/disable")
//    public ApiResponseJSON<Toggle2FAResponse> disable2FA(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
//        Toggle2FAResponse response = authUseCases.disable2FA(authenticatedUser.getUserId());
//        return new ApiResponseJSON<>("2FA disabled successfully", response);
//    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequestJSON {

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be empty")
        private String email;

        @NotBlank(message = "Password cannot be empty")
        private String password;

        public LoginRequest toRequest() {
            return new LoginRequest(email, password);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequestJSON {

        @NotBlank(message = "Current password cannot be empty")
        private String currentPassword;

        @NotBlank(message = "New password cannot be empty")
        private String newPassword;

        @NotBlank(message = "Password confirmation cannot be empty")
        private String confirmPassword;

        public ChangePasswordRequest toRequest() {
            return new ChangePasswordRequest(currentPassword, newPassword, confirmPassword);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Toggle2FARequestJSON {

        @NotBlank(message = "2FA method cannot be empty")
        private String twoFAMethod;

        public Toggle2FARequest toRequest() {
            return new Toggle2FARequest(twoFAMethod);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token2FaRequestJSON {
        @NotBlank(message = "Token cannot be empty")
        private String token;
    }

    @Data
    public static class ChangePasswordOnLoginRequestJSON{
        @NotBlank(message = "New password cannot be empty")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                 message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
        String newPassword;
    }
}
