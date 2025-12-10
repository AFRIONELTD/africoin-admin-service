package com.afrione.africoinservice.domain.entities.enums;

public enum CurrencyTypeConstant {
    USD("US Dollar"),
    EUR("Euro"),
    GBP("British Pound"),
    JPY("Japanese Yen"),
    CAD("Canadian Dollar"),
    AUD("Australian Dollar"),
    CHF("Swiss Franc"),
    CNY("Chinese Yuan"),
    INR("Indian Rupee"),
    ZAR("South African Rand"),
    NGN("Nigerian Naira"),
    GHS("Ghanaian Cedi"),
    KES("Kenyan Shilling");

    private final String displayName;

    CurrencyTypeConstant(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

