package com.afrione.africoinservice.usecases.data.value_objects;

import com.afrione.africoinservice.domain.entities.enums.ClientTypeConstant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MobileAuthDetail {
    private String password;
    private String authenticationKey;
    private String deviceUniqueId;
    private String deviceModel;
    private ClientTypeConstant clientType;
}
