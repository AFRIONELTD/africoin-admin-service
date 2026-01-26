package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.OrderHistoryUseCases;
import com.afrione.africoinservice.usecases.data.response.otc.OrderSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Returns dashboard summary")
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<List<OrderSummaryResponse>> getOrderSummary(
            @Parameter(description = "GHS|NGN") @Pattern(regexp = "GHS|NGN") @RequestParam String corridor,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate endDate) {
        List<OrderSummaryResponse> response = orderHistoryUseCases.getOrderSummary("ON_RAMP", corridor, startDate, endDate);
        return new ApiResponseJSON<>("Order summary returned successfully.", response);
    }
}
