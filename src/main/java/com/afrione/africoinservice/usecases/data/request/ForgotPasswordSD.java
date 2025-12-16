package com.afrione.africoinservice.usecases.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by felixadewale on
 * 16/12/2025
 */
@Data @AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordSD {
    private Long userId;
    private String encryptedToken;
    private int expiryInSeconds;
    private int tokenTrial;
    private boolean verified;
}
