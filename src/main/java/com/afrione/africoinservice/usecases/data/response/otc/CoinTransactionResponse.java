package com.afrione.africoinservice.usecases.data.response.otc;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CoinTransactionResponse {

    private Long transactionId;
    private String transactionReference;
    private String date;
    private String paymentStatus;
    private String fundingStatus;
    private String payoutStatus;
    private String transferStatus;
    private String debitStatus;
    private String blockChain;
    private BigDecimal coinAmount;
    private String fiatAmount;
    private BigDecimal rate;
    private String recipientName;
    private String senderName;
}
