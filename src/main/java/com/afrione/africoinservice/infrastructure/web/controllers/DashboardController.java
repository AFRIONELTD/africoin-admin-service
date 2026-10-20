package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.CustomerReadUseCases;
import com.afrione.africoinservice.usecases.DashboardUseCases;
import com.afrione.africoinservice.usecases.OrderHistoryUseCases;
import com.afrione.africoinservice.usecases.data.response.dashboard.CrossBorderSummaryResponse;
import com.afrione.africoinservice.usecases.data.response.dashboard.OrderGraphResponse;
import com.afrione.africoinservice.usecases.data.response.otc.MonthStatsWrapper;
import com.afrione.africoinservice.usecases.data.response.otc.OrderSummaryResponse;
import com.afrione.africoinservice.usecases.data.response.otc.UserSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by felixadewale on
 * 12/01/2026
 */

@Slf4j
@Validated
@Transactional
@Tag(name = "Dashboard Endpoints for Admin", description = "Handles dashboard operations")
@RestController
@RequestMapping(value = "/api/v1/admin/dashboard", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class DashboardController {

    private final OrderHistoryUseCases orderHistoryUseCases;
    private final DashboardUseCases dashboardUseCases;
    private final CustomerReadUseCases customerReadUseCases;

    @Operation(summary = "Returns dashboard summary")
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<List<OrderSummaryResponse>> getOrderSummary(
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser,
            @Parameter(description = "GHS|NGN|CAD|EUR|GBP") @Pattern(regexp = "GHS|NGN|CAD|EUR|GBP") @RequestParam String corridor,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate endDate) {
        List<OrderSummaryResponse> response = orderHistoryUseCases.getOrderSummary(authenticatedUser, "ON_RAMP", corridor, startDate, endDate);
        return new ApiResponseJSON<>("Order summary returned successfully.", response);
    }

    @GetMapping("/crossborder")
    @Operation(summary = "Get Cross Border Analytics",
            description = "Get total amounts received per fiat currency within a given date range")
    public ApiResponseJSON<CrossBorderSummaryResponse> getCrossBoarderAnalytics(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser,  @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate startDate,
                                                                                @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate endDate) {

        CrossBorderSummaryResponse res = dashboardUseCases.getCrossBoarderAnalytics(authenticatedUser, startDate, endDate);
        return new ApiResponseJSON<>("successful", res);
    }

    @GetMapping("/order-graph")
    @Operation(summary = "Get Order Graph Data",
            description = "Get order graph data within a given date range")
    public ApiResponseJSON<OrderGraphResponse> getOrderGraphData(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser, @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate startDate,
                                                                        @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate endDate) {

        OrderGraphResponse response = dashboardUseCases.getOrderGraphData(authenticatedUser, startDate, endDate);
        return new ApiResponseJSON<>("successful", response);
    }


    @GetMapping(value = "/user-device-stats")
    public ResponseEntity<ApiResponseJSON<UserSummaryResponse>> getUserDeviceStat(@RequestParam(required = false) @Parameter(description = "GH|NG") @Pattern(regexp = "GH|NG") String countryCode,
                                                                                  @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                                  @RequestParam(required = false) @Schema(description = "yyyy-MM-dd") @DateTimeFormat( pattern = "yyyy-MM-dd") LocalDate endDate,
                                                                                  @AuthenticationPrincipal AuthenticatedUser authenticatedUser){


        UserSummaryResponse response = customerReadUseCases.getUserStatsByDevice(countryCode, startDate, endDate ,authenticatedUser);
        return ResponseEntity.ok(new ApiResponseJSON<>("successful", response));

    }


    @GetMapping(value = "/afri-month-graph")
    @Operation(description = "get month graph data")
    public ResponseEntity<ApiResponseJSON<MonthStatsWrapper>> adminPortalGraph(@RequestParam(required = false) @Max(value = 12) @Min(value = 1) @Parameter(description = "value between 1 -12 ") Integer startMonth,
                                                                               @RequestParam(required = false) @Max(value = 12) @Min(value = 1) @Parameter(description = "value between 1 -12 ")   Integer endMonth,
                                                                               @Pattern(regexp = "^\\d{4}$" , message = "please provide a valid year number") Integer year, @Schema(description ="GH|NG" ) @Pattern(regexp = ("GH|NG")) @RequestParam(required = false)  String countryCode,
                                                                               @AuthenticationPrincipal AuthenticatedUser authenticatedUser){


        MonthStatsWrapper response = dashboardUseCases.getMonthStats(startMonth, endMonth, year, countryCode, authenticatedUser);

        return ResponseEntity.ok(new ApiResponseJSON<>("successful", response));
    }
}
