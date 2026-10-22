package com.afrione.africoinservice.usecases.feature.customer.notification;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.usecases.ExternalRequestUseCases;
import com.afrione.africoinservice.usecases.data.request.NotificationRequest;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.NotificationConstant;
import com.afrione.africoinservice.usecases.data.response.otc.NotificationResponse;

import java.time.LocalDate;

public interface NotificationUseCases extends ExternalRequestUseCases {

    void deleteNotification(Long id, AuthenticatedUser authenticatedUser);

    NotificationConstant getConstant(AuthenticatedUser authenticatedUser);

    NotificationResponse notificationDetail(Long id , AuthenticatedUser authenticatedUser);

    PagedResponse<NotificationResponse> fetchNotification(LocalDate startDate, LocalDate endDate, String keyword, String audience, String notificationTYpe , int pageNo, int pageSie, AuthenticatedUser authenticatedUser);

    Long getUserCount(String audience ,AuthenticatedUser authenticatedUser);

    NotificationResponse editNotification(NotificationRequest request, Long id , AuthenticatedUser authenticatedUser);

    NotificationResponse createNotification(NotificationRequest request, AuthenticatedUser authenticatedUser);
}
