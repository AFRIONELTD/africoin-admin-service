package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.domain.services.ApplicationProperty;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by felixadewale on
 * 18/12/2025
 */
public interface ExternalRequestUseCases {

    default boolean isSuccessful(HttpStatusCode statusCode) {
        return statusCode != null && statusCode.is2xxSuccessful();
    }

    private String generateToken(String username, String secretKey) {

        try {
            long timestamp = Instant.now().getEpochSecond();
            String data = username + ":" + timestamp;
            String hmac = hmacSha256(data, secretKey);

            String tokenString = username + ":" + timestamp + ":" + hmac;
            return Base64.getEncoder().encodeToString(tokenString.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    private String hmacSha256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    default Map<String, String> generateMerchantHeader(String adminUser) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-request-client-key", getApplicationProperty().getB2BRequestClientKey());
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.put("Authorization", "Bearer " + generateToken(adminUser, getApplicationProperty().getMerchantTokenSecretKey()));
        return headers;
    }

    default Map<String, String> generateClientHeader(String adminUser) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-request-client-key", getApplicationProperty().getB2CRequestClientKey());
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.put("Authorization", "Bearer " + generateToken(adminUser, getApplicationProperty().geCustomerTokenSecretKey()));
        return headers;
    }

    ApplicationProperty getApplicationProperty();

}
