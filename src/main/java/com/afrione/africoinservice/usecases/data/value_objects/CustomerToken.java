package com.afrione.africoinservice.usecases.data.value_objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerToken {
    private AppToken accessToken;
    private AppToken refreshToken;
}
