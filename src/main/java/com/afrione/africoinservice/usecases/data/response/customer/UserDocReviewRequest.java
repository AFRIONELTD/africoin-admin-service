package com.afrione.africoinservice.usecases.data.response.customer;

import jakarta.validation.constraints.Pattern;
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
    Long userId;
    String reviewComment;
    Boolean isApproved;
    @Pattern(regexp = "SELFIE|ID")
    String fileType;
}
