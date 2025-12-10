package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.AccountSetupUseCases;
import com.afrione.africoinservice.usecases.data.request.AccountSetupRequest;
import com.afrione.africoinservice.usecases.data.response.account_setup.AccountSetupResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("setup-account")
    public ApiResponseJSON<AccountSetupResponse> setupAccount(@RequestBody @Valid AccountSetupRequestJSON request, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        AccountSetupResponse accountSetupResponse = accountSetupUseCases.setupAccount(request.toRequest(), authenticatedUser.getUserId());
        return new ApiResponseJSON<>("Account setup successful", accountSetupResponse);
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
        private Set<String> roles;

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
}
