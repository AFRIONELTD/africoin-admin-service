package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Data;

@Data
public class FiatCurrencyModel {
    private String name;
    private String symbol;
    private String shortName;
    private String code;
    private String logoUrl;

    public FiatCurrencyModel(String name, String symbol, String code) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
    }

    public FiatCurrencyModel(
            String name, String symbol, String code, String shortName, String logoUrl) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
        this.shortName = shortName;
        this.logoUrl = logoUrl;
    }
}

