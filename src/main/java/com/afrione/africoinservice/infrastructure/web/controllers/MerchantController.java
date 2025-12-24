package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.MerchantReadUseCases;
import com.afrione.africoinservice.usecases.MerchantWriteUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.AdminMerchantResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.afrione.africoinservice.usecases.data.request.KycApprovalRequest;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
@Transactional
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/merchant/", produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Authorization"})
@RestController
@Validated
@Slf4j
public class MerchantController {

    private final MerchantReadUseCases readUseCases;
    private final MerchantWriteUseCases writeUseCases;

    @GetMapping(value = "retrieve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<PagedResponse<AdminMerchantResponse>> retrieveMerchants(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) @Pattern(
                    regexp = "^(UNVERIFIED|UPLOAD_IN_PROGRESS|PENDING_VERIFICATION|VERIFIED|REJECTED)$",
                    message = "Invalid verification status"
            )
            String searchStatus,
            @RequestParam(required = false) String countryCode,
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNo,
            @RequestParam(defaultValue = "10") @Max(value = 50, message = "Max page size is 50") @Positive int pageSize
    ) {
        PagedResponse<AdminMerchantResponse> response = readUseCases.retrieveMerchants(searchTerm, searchStatus, countryCode, pageNo, pageSize, authenticatedUser.getAccountId());
        return new ApiResponseJSON<>("Data fetched successfully", response);
    }

    @GetMapping(value = "retrieve/{merchantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<AdminMerchantResponse> retrieveMerchant(@PathVariable String merchantId, @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        AdminMerchantResponse response = readUseCases.retrieveMerchant(merchantId, AdminMerchantResponse.DetailLevel.FULL, authenticatedUser.getUserId());
        return new ApiResponseJSON<>("Data fetched successfully", response);
    }

    @PostMapping(value = "approve-or-decline-document", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Approve or decline merchant KYC document.")
    public ResponseEntity<ApiResponseJSON<Void>> approveOrDeclineDocument(
            @RequestBody @Valid KycApprovalRequestJSON kycApprovalRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

        log.info("Received KYC approval request for merchant: {} and fileId: {}",
                kycApprovalRequest.getMerchantId(), kycApprovalRequest.getFileId());

        writeUseCases.approveOrDeclineMerchantDocument(kycApprovalRequest.toRequest(), authenticatedUser.getAccountId());

        return ResponseEntity.ok(ApiResponseJSON.<Void>builder()
                .message("Document approval status updated successfully.")
                .build());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KycApprovalRequestJSON {
        private boolean approved;
        private boolean director;
        private String comment;

        @NotBlank(message = "File ID cannot be empty")
        private String fileId;

        @NotBlank(message = "Merchant ID cannot be empty")
        private String merchantId;

        public KycApprovalRequest toRequest() {
            if (!approved && StringUtils.isBlank(comment)) {
                throw new BadRequestException("Comment is required when declining a document");
            }
            return KycApprovalRequest.builder()
                    .approved(approved)
                    .comment(comment)
                    .fileId(fileId)
                    .isDirector(director)
                    .merchantId(merchantId)
                    .build();
        }
    }
}
