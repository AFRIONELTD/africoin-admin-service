package com.afrione.africoinservice.usecases.data.response.otc;

import java.math.BigDecimal;

public record CoinBalanceResponse(BigDecimal coinBalance, String blockchain) {
}
