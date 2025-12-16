package com.afrione.africoinservice.usecases.data.response.auth;

/**
 * Created by felixadewale on
 * 16/12/2025
 */
public record ForgotPasswordResponse(String sessionId, int expiresInSeconds) {
}
