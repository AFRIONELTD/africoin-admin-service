package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.OrderHistoryResponse;
import com.afrione.africoinservice.usecases.data.response.otc.OrderSummaryResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by felixadewale on
 * 15/01/2026
 */
public interface OrderHistoryUseCases extends ExternalRequestUseCases{
    PagedResponse<OrderHistoryResponse> getOrderHistory(AuthenticatedUser authenticatedUser, String orderType, LocalDate startDate, LocalDate endDate, String corridor, String status, int page, int size);

    OrderHistoryResponse getOrderDetail(AuthenticatedUser authenticatedUser, String orderId, String orderType);

    List<OrderSummaryResponse> getOrderSummary(AuthenticatedUser authenticatedUser, String orderType, String corridor, LocalDate startDate, LocalDate endDate);
}
