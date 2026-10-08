package com.afrione.africoinservice.usecases.data.response.otc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CoinInfoResponse(BigDecimal CoinInCirculation, BigDecimal afriWalletBalance, BigDecimal prefundedUsdBalance) {
}
