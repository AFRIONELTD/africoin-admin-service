package com.afrione.africoinservice.infrastructure.web.models;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ApiResponseJSON<T> {
    public ApiResponseJSON(String message) {
        this.message = message;
        this.data = null;
    }
    public ApiResponseJSON(String message, T data) {
        this.message = message;
        this.data = data;
    }

    private String message;
    private T data;
}
