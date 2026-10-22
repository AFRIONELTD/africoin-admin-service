package com.afrione.africoinservice.usecases.data.response.otc;

public record MonthStatsWrapper(MonthStatsResponse funding, MonthStatsResponse transfer, MonthStatsResponse payout) {
}
