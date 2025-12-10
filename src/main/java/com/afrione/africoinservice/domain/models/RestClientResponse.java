package com.afrione.africoinservice.domain.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

/**
 * Created by jnya on
 * Mon, 01 Sept, 2025
 */
@Data
@Builder
public class RestClientResponse {
    private String responseBody;
    private HttpStatusCode statusCode;
    private long timeTakenInSeconds;
    private Object responseObject;
}
