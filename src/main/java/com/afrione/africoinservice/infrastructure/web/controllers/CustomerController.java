package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.CustomerReadUseCases;
import com.afrione.africoinservice.usecases.CustomerWriteUseCases;
import com.afrione.africoinservice.usecases.data.response.customer.AppUserModel;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.customer.UserDocReviewRequest;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
@Transactional
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/customer/", produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Authorization"})
@RestController
@Validated
@Slf4j
public class CustomerController {

    private final CustomerReadUseCases readUseCases;
    private final CustomerWriteUseCases writeUseCases;


    @GetMapping("/users")
    public ApiResponseJSON<PagedResponse<AppUserModel>> getAppUsers(@RequestParam(required = false) String searchTerm,
                                                                    @RequestParam(required = false) @Pattern(
                                                                            regexp = "^(UNVERIFIED|UPLOAD_IN_PROGRESS|PENDING_VERIFICATION|VERIFIED|REJECTED)$",
                                                                            message = "Invalid status"
                                                                    ) String searchStatus,
                                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int pageNo,
                                                                    @RequestParam(defaultValue = "10") @Max(value = 50, message = "Max page size is 50") @Positive int pageSize, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {

        PagedResponse<AppUserModel> pagedResponse = readUseCases.listUsers(searchTerm, searchStatus, pageNo, pageSize, authenticatedUser.getAccountId());
        return new ApiResponseJSON<>("Users fetched successfully", pagedResponse);

    }

    @GetMapping("/user/{userId}")
    public ApiResponseJSON<AppUserModel> getAppUser(@PathVariable String userId, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        AppUserModel appUserModelExtended = readUseCases.getAppUser(userId, authenticatedUser.getUserId());
        return new ApiResponseJSON<>("User fetched successfully", appUserModelExtended);
    }


    @PostMapping("review")
    public ApiResponseJSON<Void> reviewUserDocument(@RequestBody @Valid UserDocReviewRequestJSON userDocReviewRequestJSON, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        writeUseCases.reviewUserDocument(userDocReviewRequestJSON.toRequest(), authenticatedUser.getAccountId());
        return new ApiResponseJSON<>("User document reviewed successfully", null);
    }


    @Data
    public static class UserDocReviewRequestJSON {
        @NotNull
        @Positive
        Long verificationId;
        @NotBlank
        String userId;
        String reviewComment;
        @NotNull
        Boolean isApproved;
        @NotBlank
        @Pattern(regexp = "SELFIE|ID")
        String fileType;

        public UserDocReviewRequest toRequest() {
            return UserDocReviewRequest.builder()
                    .verificationId(verificationId)
                    .userId(userId)
                    .reviewComment(reviewComment)
                    .isApproved(isApproved)
                    .fileType(fileType)
                    .build();
        }
    }


}
