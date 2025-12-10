package com.afrione.africoinservice.usecases.data.value_objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AppToken {
    private String token;
    private int expiryTimeInMinutes;
}
