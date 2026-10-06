package com.afrione.africoinservice.usecases.data.response.otc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CoinCirculationResponse(BigDecimal amount, OffsetDateTime lastUpdated, int failedWallerRetrievalCount) {
}
