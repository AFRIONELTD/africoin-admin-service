package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.domain.entities.enums.CryptoCurrencyTypeConstant;
import com.afrione.africoinservice.domain.entities.enums.CurrencyTypeConstant;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.MerchantReadUseCases;
import com.afrione.africoinservice.usecases.MerchantWriteUseCases;
import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoFiatRateResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
@Transactional
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/rate", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@Validated
public class RateController {

    private final MerchantWriteUseCases merchantWriteUseCases;
    private final MerchantReadUseCases merchantReadUseCases;

    @GetMapping(value = "{cryptoCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<List<CryptoFiatRateResponse>> retrieveRate(@PathVariable String cryptoCurrency, @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        List<CryptoFiatRateResponse> response = merchantReadUseCases.retrieveRate(cryptoCurrency, authenticatedUser.getAccountId());
        return new ApiResponseJSON<>("Data fetched successfully", response);
    }


    @PostMapping(value = "/update/{cryptoCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update exchange rates for cryptocurrencies.")
    public ResponseEntity<ApiResponseJSON<Void>> updateExchangeRate(@RequestBody @Valid List<ExchangeRateUpdateRequestJSON> requestJSON, @PathVariable String cryptoCurrency, @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        merchantWriteUseCases.updateRates(requestJSON.stream().map(rq -> rq.toRequest(cryptoCurrency)).toList(), authenticatedUser.getAccountId());
        return ResponseEntity.ok(ApiResponseJSON.<Void>builder()
                .message("Exchange rate updated successfully.")
                .build());
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

        public ExchangeRateUpdateRequest toRequest(String cryptoCurrency) {
            CryptoCurrencyTypeConstant currencyTypeConstant;
            try {
                currencyTypeConstant = CryptoCurrencyTypeConstant.valueOf(cryptoCurrency);
            } catch (Exception e) {
                throw new BadRequestException("Unsupported crypto currency type " + cryptoCurrency);
            }
            return new ExchangeRateUpdateRequest(currencyTypeConstant,
                    CurrencyTypeConstant.valueOf(fiatCurrency),
                    newRate,
                    markup);
        }
    }

}
