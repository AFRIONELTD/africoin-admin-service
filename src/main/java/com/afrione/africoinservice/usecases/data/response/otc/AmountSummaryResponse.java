package com.afrione.africoinservice.usecases.data.response.otc;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class AmountSummaryResponse {

     private BigDecimal afriAmount;

     private List<AmountSummary> paymentAmount;
}
