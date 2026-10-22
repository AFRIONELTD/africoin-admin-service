package com.afrione.africoinservice.usecases.data.request;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class NotificationRequest {

    private Long id;

    private String title;

    private String message;

    private String audience;

    private String notificationType;

    private Long recipientCount;

    private String status;

    private Instant time;

    private boolean now;
}
