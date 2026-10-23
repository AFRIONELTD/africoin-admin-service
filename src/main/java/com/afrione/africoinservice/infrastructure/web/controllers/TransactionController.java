package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.*;
import com.afrione.africoinservice.usecases.feature.customer.transaction.TransactionUseCases;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "api/v1/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionUseCases transactionUseCases;

    @GetMapping(value = "/funding-summary")
    public ResponseEntity<ApiResponseJSON<AmountSummaryResponse>> getAfriAndPaymentSummaryForFunding(
                                    @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
                                    @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                    @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
                                    @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        AmountSummaryResponse response = transactionUseCases.getAfriAndPaymentSummaryForFunding(startDate, endDate, countryCode, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));

    }

    @GetMapping(value = "/transfer-summary")
    public ResponseEntity<ApiResponseJSON<AmountSummaryResponse>> getAfriAndPaymentSummaryForTransfer(
                                @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
                                @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
                                @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        AmountSummaryResponse response = transactionUseCases.getAfriAndPaymentSummaryForTransfer(startDate, endDate, countryCode, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));

    }

    @GetMapping(value = "/payout-summary")
    public ResponseEntity<ApiResponseJSON<AmountSummaryResponse>> getAfriAndPaymentSummaryForPayout(
                            @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
                            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
                            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        AmountSummaryResponse response = transactionUseCases.getAfriAndPaymentSummaryForPayout(startDate, endDate, countryCode, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));

    }

    @GetMapping(value = "/funding")
    public ResponseEntity<ApiResponseJSON<PagedResponse<CoinFundingTransactionResponse>>> fetchFundingTransaction(
            @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNo,
            @RequestParam(defaultValue = "10") @Positive int pageSize,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


       PagedResponse<CoinFundingTransactionResponse> response = transactionUseCases.fetchCoinFundingTransactions(startDate,endDate,countryCode, pageNo, pageSize, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));
    }

    @GetMapping(value = "/download-funding-report")
    public ResponseEntity<ApiResponseJSON<ExportResult>> downloadFundingReport(
            @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        ExportResult response = transactionUseCases.downloadCoinFundingReport(startDate,endDate,countryCode, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("processed successfully", response));
    }

    @GetMapping(value = "/transfer")
    public ResponseEntity<ApiResponseJSON<PagedResponse<CoinTransferTransactionResponse>>> fetchTransferTransaction(
            @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNo,
            @RequestParam(defaultValue = "10") @Positive int pageSize,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){

       PagedResponse<CoinTransferTransactionResponse> response = transactionUseCases.fetchTransferTransactions(startDate,endDate, countryCode, pageNo, pageSize, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));
    }

    @GetMapping(value = "/download-transfer-report")
    public ResponseEntity<ApiResponseJSON<ExportResult>> downloadTransferReport(
            @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){

        ExportResult response = transactionUseCases.downloadTransferReport(startDate,endDate, countryCode,  authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Processed successfully", response));
    }

    @GetMapping(value = "/payout")
    public ResponseEntity<ApiResponseJSON<PagedResponse<CoinPayoutTransactionResponse>>> fetchPayoutTransaction(
            @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNo,
            @RequestParam(defaultValue = "10") @Positive int pageSize,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        PagedResponse<CoinPayoutTransactionResponse> response = transactionUseCases.fetchPayoutTransactions(startDate, endDate, countryCode, pageNo, pageSize, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));
    }


    @GetMapping(value = "/download-payout-report")
    public ResponseEntity<ApiResponseJSON<ExportResult>> downloadPayoutReport(
            @Schema(description ="NG|GH" ) @Pattern(regexp = ("NG|GH")) @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        ExportResult response = transactionUseCases.downloadPayoutReport(startDate, endDate, countryCode, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Processed successfully", response));
    }

    @GetMapping(value = "/payout/{id}")
    public ResponseEntity<ApiResponseJSON<CoinPayoutTransactionResponse>> getPayoutTransactionDetails(@PathVariable Long id,  @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        CoinPayoutTransactionResponse response = transactionUseCases.fetchPayoutTransactionDetails(id, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));
    }


    @GetMapping(value = "/transfer/{id}")
    public ResponseEntity<ApiResponseJSON<CoinTransferTransactionResponse>> getTransferTransactionDetails(@PathVariable Long id,  @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        CoinTransferTransactionResponse response = transactionUseCases.fetchTransferTransactionDetails(id, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));
    }


    @GetMapping(value = "/funding/{id}")
    public ResponseEntity<ApiResponseJSON<CoinFundingTransactionResponse>> getFundingTransactionDetails(@PathVariable Long id,  @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser ){


        CoinFundingTransactionResponse response = transactionUseCases.fetchFundTransactionDetails(id, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Retrieve successfully", response));
    }
}
