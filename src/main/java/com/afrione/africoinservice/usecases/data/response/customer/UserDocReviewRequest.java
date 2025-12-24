package com.afrione.africoinservice.usecases.data.response.customer;

import lombok.Builder;
import lombok.Value;

/**
 * Created by felixadewale on
 * 15/12/2025
 */
@Builder
@Value
public class UserDocReviewRequest {
    Long verificationId;
    String userId;
    String reviewComment;
    Boolean isApproved;
    String fileType;
}
