package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.domain.entities.enums.CurrencyTypeConstant;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.CustomerReadUseCases;
import com.afrione.africoinservice.usecases.CustomerWriteUseCases;
import com.afrione.africoinservice.usecases.MerchantReadUseCases;
import com.afrione.africoinservice.usecases.MerchantWriteUseCases;
import com.afrione.africoinservice.usecases.data.request.B2cExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoRateResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.RateStatsModel;
import com.afrione.africoinservice.usecases.data.response.otc.ExchangeRateHistoryResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
@Transactional
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/rate", produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Authorization"})
@RestController
@Validated
public class RateController {

    private final MerchantWriteUseCases merchantWriteUseCases;
    private final MerchantReadUseCases merchantReadUseCases;
    private final CustomerWriteUseCases customerWriteUseCases;
    private final CustomerReadUseCases customerReadUseCases;

    @GetMapping(value = "/{service}/retrieve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<List<CryptoRateResponse>> retrieveRate(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser, @PathVariable @Pattern(regexp = "OTC|MERCHANT") @Parameter(description = "OTC|MERCHANT") String service) {
        List<CryptoRateResponse> response =
                isMerchantService(service) ?
                        merchantReadUseCases.retrieveRate(authenticatedUser.getAccountId()) :
                        customerReadUseCases.retrieveRate(authenticatedUser.getAccountId());
        return new ApiResponseJSON<>("Data fetched successfully", response);
    }


    @GetMapping(value = "/b2c/history", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Fetch exchange rate history with optional filters for cryptocurrency, fiat currency, and date range.")
    public ResponseEntity<ApiResponseJSON<PagedResponse<ExchangeRateHistoryResponse>>> fetchExchangeRateHistory(@RequestParam(required = false) String cryptoCurrency,
                                                                                               @RequestParam(required = false) String fiatCurrency,
                                                                                               @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                                               @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
                                                                                               @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
                                                                                                             @Parameter(hidden = true)   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

        PagedResponse<ExchangeRateHistoryResponse> response = customerReadUseCases.fetchExchangeRateHistory( fiatCurrency, cryptoCurrency, startDate, endDate, pageNo, pageSize, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("Exchange rate history fetched successfully", response));

    }


    @PostMapping(value = "/{service}/update/{cryptoCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update exchange rates for cryptocurrencies.")
    public ResponseEntity<ApiResponseJSON<Void>> updateExchangeRate(@RequestBody @Valid List<ExchangeRateUpdateRequestJSON> requestJSON, @Pattern(regexp = "AFRi_TRC20|AFRi_ERC20|AFRi_SPL") @PathVariable String cryptoCurrency, @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser, @PathVariable @Pattern(regexp = "OTC|MERCHANT") @Parameter(description = "OTC|MERCHANT") String service) {
        List<ExchangeRateUpdateRequest> exchangeRateUpdateRequests = requestJSON.stream().map(ExchangeRateUpdateRequestJSON::toRequest).toList();
        if (isMerchantService(service)) {
            merchantWriteUseCases.updateRates(exchangeRateUpdateRequests, cryptoCurrency, authenticatedUser.getAccountId());
        } else {
            throw new BadRequestException("End point is no longer in use for OTC. Please use /b2c/update/{cryptoCurrency} instead.");
        }
        return ResponseEntity.ok(ApiResponseJSON.<Void>builder()
                .message("Exchange rate updated successfully.")
                .build());
    }

    @PostMapping(value = "/b2c/update/{cryptoCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update exchange rates for cryptocurrencies.")
    public ResponseEntity<ApiResponseJSON<Void>> updateB2CExchangeRate(@RequestBody @Valid B2cExchangeRateUpdateRequestJSON requestJSON, @Pattern(regexp = "AFRi_TRC20|AFRi_ERC20|AFRi_SPL") @PathVariable String cryptoCurrency,
                                                                       @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {


            customerWriteUseCases.updateRates(requestJSON.toRequest(), cryptoCurrency, authenticatedUser.getAccountId());

        return ResponseEntity.ok(ApiResponseJSON.<Void>builder()
                .message("Exchange rate updated successfully.")
                .build());
    }

    @Operation(summary = "Returns basic rate stats: active currencies count, average markup, last updated")
    @GetMapping(value = "/{service}/rate-stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<RateStatsModel> getRateStats(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser, @RequestParam(required = false) String fiat, @PathVariable @Pattern(regexp = "OTC|MERCHANT") @Parameter(description = "OTC|MERCHANT") String service) {
        RateStatsModel stats =
                isMerchantService(service) ?
                        merchantReadUseCases.retrieveRateStats(fiat, authenticatedUser.getAccountId()) :
                        customerReadUseCases.retrieveRateStats(fiat, authenticatedUser.getAccountId());
        return new ApiResponseJSON<>("Rate stats returned successfully", stats);
    }


    @Data
    public static class ExchangeRateUpdateRequestJSON {
        @NotBlank(message = "Fiat currency is required")
        String fiatCurrency;
        @Positive(message = "New rate must be positive")
        @NotNull(message = "New rate is required")
        BigDecimal newRate;
        @NotNull(message = "Markup is required")
        @PositiveOrZero(message = "Markup must be zero or positive")
        BigDecimal markup;

        public ExchangeRateUpdateRequest toRequest() {

            return new ExchangeRateUpdateRequest(
                    CurrencyTypeConstant.valueOf(fiatCurrency),
                    newRate,
                    markup);
        }
    }
    public static class B2cExchangeRateUpdateRequestJSON {

        @NotBlank(message = "Fiat currency is required")
        String fiatCurrency;

        @Positive(message = "New rate must be positive")
        @NotNull(message = "OnRamp rate is required")
        BigDecimal onRampRate;

        @Positive(message = "New rate must be positive")
        @NotNull(message = "OnRamp rate is required")
        BigDecimal offRampRate;

        @NotNull(message = "Send notification flag is required")
        Boolean sendNotification;

        String reason;



        public B2cExchangeRateUpdateRequest toRequest(){

            return new B2cExchangeRateUpdateRequest(fiatCurrency, onRampRate, offRampRate, sendNotification, reason);
        }

    }


    private boolean isMerchantService(String service) {
        return "MERCHANT".equalsIgnoreCase(service);
    }

}
