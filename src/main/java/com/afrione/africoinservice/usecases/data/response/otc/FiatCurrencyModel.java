package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Data;

@Data
public class FiatCurrencyModel {
    private String name;
    private String symbol;
    private String shortName;
    private String code;
    private String logoUrl;
    private String country;

}

