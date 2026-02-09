package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.usecases.data.response.dashboard.CrossBorderSummaryResponse;
import com.afrione.africoinservice.usecases.data.response.dashboard.OrderGraphResponse;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by felixadewale on
 * 26/01/2026
 */
public interface DashboardUseCases extends ExternalRequestUseCases {
    CrossBorderSummaryResponse getCrossBoarderAnalytics(AuthenticatedUser authenticatedUser, LocalDate startDate, LocalDate endDate);

    ApplicationProperty getApplicationProperty();

    OrderGraphResponse getOrderGraphData(AuthenticatedUser authenticatedUser, LocalDate startDate, LocalDate endDate);
}
