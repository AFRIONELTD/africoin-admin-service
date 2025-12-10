package com.afrione.africoinservice.usecases.data.response.login;

import lombok.Builder;
import lombok.Data;

/**
 * Created by jnya on
 * Tue, 16 Sept, 2025
 */
@Data
@Builder
public class WebLoginResponse {
    private String sessionId;
    private int validityInMinutes;
}
