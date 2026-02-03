package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.OrderHistoryUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.OrderHistoryResponse;
import com.afrione.africoinservice.usecases.data.response.otc.OrderSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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
@Tag(name = "Order Endpoints for Admin", description = "Handles order operations")
@RestController
@RequestMapping(value = "/api/v1/admin/order", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryUseCases orderHistoryUseCases;

    @Operation(summary = "Returns paginated list of orders.")
    @GetMapping(value = "/OTC/{orderType}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<PagedResponse<OrderHistoryResponse>> getOrderHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable @Pattern(regexp = "OFF_RAMP|ON_RAMP") String orderType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate endDate,
            @Parameter(description = "GHS|NGN|CAD|EUR|GBP") @Pattern(regexp = "GHS|NGN|CAD|EUR|GBP") @RequestParam(required = false) String corridor,
            @Parameter(description = "PENDING|FAILED|SUCCESSFUL") @Pattern(regexp = "PENDING|FAILED|SUCCESSFUL") @RequestParam(required = false) String status,
            @Parameter(description = "No. of records per page. Min:1, Max:20") @Valid @Min(value = 1) @Max(value = 100) @RequestParam(value = "size", defaultValue = "20") int size,
            @Parameter(description = "The index of the page to return. Min: 0") @Valid @Min(value = 0) @RequestParam(value = "page", defaultValue = "0") int page) {
        PagedResponse<OrderHistoryResponse> response = orderHistoryUseCases.getOrderHistory(authenticatedUser, orderType, startDate, endDate, corridor, status, page, size);
        return new ApiResponseJSON<>("Order history returned successfully.", response);
    }

    @Operation(summary = "Returns full order detail")
    @GetMapping(value = "/OTC/{orderId}/{orderType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<OrderHistoryResponse> getOrderDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable String orderId, @PathVariable @Pattern(regexp = "OFF_RAMP|ON_RAMP") String orderType) {
        OrderHistoryResponse response = orderHistoryUseCases.getOrderDetail(authenticatedUser, orderId, orderType);
        return new ApiResponseJSON<>("Order returned successfully.", response);
    }

    @Operation(summary = "Returns order summary")
    @GetMapping(value = "/OTC/{orderType}/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseJSON<List<OrderSummaryResponse>> getOrderSummary(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable @Pattern(regexp = "OFF_RAMP|ON_RAMP") String orderType,
            @Parameter(description = "GHS|NGN|CAD|EUR|GBP") @Pattern(regexp = "GHS|NGN|CAD|EUR|GBP") @RequestParam String corridor,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") @PastOrPresent LocalDate endDate) {
        List<OrderSummaryResponse> response = orderHistoryUseCases.getOrderSummary(authenticatedUser, orderType, corridor, startDate, endDate);
        return new ApiResponseJSON<>("Order summary returned successfully.", response);
    }
}
