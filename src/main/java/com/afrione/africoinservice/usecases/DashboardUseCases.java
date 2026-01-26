package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.usecases.data.response.dashboard.CrossBorderSummaryResponse;
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

    default Map<String, String> generateHeader(String adminUser) {
        System.out.println("Generating headers for admin user: " + adminUser);
        Map<String, String> headers = new HashMap<>();
        headers.put("x-request-client-key", getApplicationProperty().getB2CRequestClientKey());
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    ApplicationProperty getApplicationProperty();
}
