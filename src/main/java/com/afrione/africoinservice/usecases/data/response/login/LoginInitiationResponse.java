package com.afrione.africoinservice.usecases.data.response.login;

/**
 * Created by felixadewale on
 * 22/12/2025
 */
public record LoginInitiationResponse(String sessionId, long tokenLifeSpanInSeconds) {
}
