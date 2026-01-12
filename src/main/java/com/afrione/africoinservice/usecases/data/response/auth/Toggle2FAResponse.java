package com.afrione.africoinservice.usecases.data.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Toggle2FAResponse {
    private boolean twoFAEnabled;
    private String twoFAMethod;
    private String message;
}
