package com.afrione.africoinservice.usecases.data.response.otc;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by felixadewale on
 * 13/01/2026
 */

@Data
@Builder
public class OrderHistoryResponse {
    private String orderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    private String payerName;
    private String payerEmail;
    private String destinationCorridor;
    private BigDecimal coinAmount;
    private BigDecimal payerAmount;
    private BigDecimal recipientAmount;
    @Parameter(description = "PENDING|FAILED|SUCCESSFUL")
    private String status;
    private String sourceCorridor;
    private String recipientBankName;
    private String recipientAccountName;
    private String note;
    private OrderTypeDetail orderTypeDetail;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class OrderTypeDetail {
        private String status;
        private BigDecimal amount;
        private String corridor;
    }


}
