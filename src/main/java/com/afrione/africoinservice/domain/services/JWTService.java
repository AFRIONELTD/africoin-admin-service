package com.afrione.africoinservice.domain.services;

import java.util.Map;


public interface JWTService {

    String expiringToken(String secretKey, Map<String, String> attributes, int minutes);
    Map<String, String> verify(String secretKey, String token);
}
