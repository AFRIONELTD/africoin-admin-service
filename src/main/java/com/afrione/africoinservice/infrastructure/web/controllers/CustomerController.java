package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.CustomerReadUseCases;
import com.afrione.africoinservice.usecases.CustomerWriteUseCases;
import com.afrione.africoinservice.usecases.data.response.otc.*;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
@Transactional
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/customer", produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Authorization"})
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
    public ApiResponseJSON<UserKycDetailModel> getAppUser(@PathVariable String userId, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        UserKycDetailModel appUserModelExtended = readUseCases.getAppUser(userId, authenticatedUser.getUserId());
        return new ApiResponseJSON<>("User fetched successfully", appUserModelExtended);
    }


    @PostMapping(value = "/review" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<String> reviewUserDocument(@RequestBody @Valid UserDocReviewRequestJSON userDocReviewRequestJSON, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
      String resp =  writeUseCases.reviewUserDocument(userDocReviewRequestJSON.toRequest(), authenticatedUser.getAccountId());
        return new ApiResponseJSON<String>("User document reviewed successfully", resp);
    }

    @GetMapping(value = "active-user" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseJSON<PagedResponse<AppUserModel>>> fetchActiveUser(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String countryCode,
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNo,
            @RequestParam(defaultValue = "10") @PositiveOrZero int pageSize,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {


        PagedResponse<AppUserModel> users = readUseCases.getActiveUsers(searchKeyword, countryCode, pageNo, pageSize, authenticatedUser);
        return ResponseEntity.ok( new ApiResponseJSON<>("Active users fetched successfully", users));
    }


    @GetMapping(value = "/wallet/{userId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseJSON<List<WalletModel>>> fetchUserWallets(@PathVariable String userId,
                                                              @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        List<WalletModel> userWallets = readUseCases.getUserWallets(userId, authenticatedUser);
        return ResponseEntity.ok(new ApiResponseJSON<>("User wallets fetched successfully", userWallets));
    }



    @GetMapping( value = "transaction/{userId}", produces = "application/json")
    public ResponseEntity<PagedResponse<CoinTransactionResponse>>  fetchUserTransactions(@PathVariable String userId,
                                                                                         @Schema(description ="ON_RAMP|OFF_RAMP|TRANSFER|RECEIVE" ) @Pattern(regexp = ("ON_RAMP|OFF_RAMP|TRANSFER|RECEIVE")) @RequestParam  String transactionType,
                                                                                         @RequestParam(defaultValue = "0") @PositiveOrZero  int pageNo,
                                                                                         @RequestParam(defaultValue = "10") @Positive int pageSize,
                                                                                         @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                                         @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
                                                                                         @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ) {

        PagedResponse<CoinTransactionResponse> response = readUseCases.getCustomerTransactions(userId, transactionType, startDate, endDate, pageNo, pageSize,authenticatedUser);

        return ResponseEntity.ok(response);
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
        @Pattern(regexp = "SELFIE|ID|POA|GENERAL|GCN", message = "Invalid file type")
        String fileType;

        public UserDocReviewRequest toRequest() {

            if(!isApproved && (reviewComment == null || reviewComment.isBlank())){
                throw new IllegalArgumentException("Review comment is required when rejecting a document");
            }


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
