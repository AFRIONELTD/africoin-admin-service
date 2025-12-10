package com.afrione.africoinservice.usecases.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycApprovalRequest {
    private String merchantId;
    private String fileId;
    private boolean approved;
    private boolean isDirector;
    private String comment;
}

