package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private NotificationAudienceModel audience;

    private NotificationTypeModel notificationType;

    private Long recipientCount;

    private Long audienceCount;

    private boolean active;

    private String time;

    private String sentBy;

    private String status;

    private String dateCreated;

}