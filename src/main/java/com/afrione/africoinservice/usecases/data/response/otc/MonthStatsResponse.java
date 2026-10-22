package com.afrione.africoinservice.usecases.data.response.otc;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class MonthStatsResponse {

    @Builder.Default
    private BigDecimal jan = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal feb = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal mar = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal apr = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal may = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal jun = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal jul = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal aug = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal sep = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal oct = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal nov = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal des = BigDecimal.ZERO;





}
