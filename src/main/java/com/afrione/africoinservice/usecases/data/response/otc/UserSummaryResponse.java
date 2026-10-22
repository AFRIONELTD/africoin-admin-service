package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserSummaryResponse {

    private Long totalUser;

    private List<DeviceSummary> deviceSummary;


    public static record DeviceSummary(String deviceType, Long userCount){

    }
}
