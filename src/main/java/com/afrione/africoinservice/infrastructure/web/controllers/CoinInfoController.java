package com.afrione.africoinservice.infrastructure.web.controllers;


import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.data.response.otc.CoinInfoResponse;
import com.afrione.africoinservice.usecases.feature.customer.coinInfo.CoinInfoUseCases;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coin-info")
@RequiredArgsConstructor
@Tag(name = "Coin Info Endpoints", description = "Handles management of coin info service.")
public class CoinInfoController {

    private final CoinInfoUseCases coinInfoUseCases;

    @GetMapping(value = "", produces = "application/json")
    @Operation(summary = "Get AfriCoin in circulation", description = "Returns the total amount of AfriCoin currently in circulation.")
    public ResponseEntity<ApiResponseJSON<CoinInfoResponse>> getCoinInCirculation(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {

        ApiResponseJSON<CoinInfoResponse> response = coinInfoUseCases.getAfriCoinInCirculation(authenticatedUser);

        return ResponseEntity.ok(response);

    }

}
