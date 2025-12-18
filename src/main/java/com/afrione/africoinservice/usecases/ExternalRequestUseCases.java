package com.afrione.africoinservice.usecases;

import org.springframework.http.HttpStatusCode;

/**
 * Created by felixadewale on
 * 18/12/2025
 */
public interface ExternalRequestUseCases {

    default boolean isSuccessful(HttpStatusCode statusCode) {
        return statusCode != null && statusCode.is2xxSuccessful();
    }
}
