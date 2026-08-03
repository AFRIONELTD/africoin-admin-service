package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Data;

@Data
public class PayoutProcessorChannelModel {
    private String name;
    private String symbol;
    private String code;

    public PayoutProcessorChannelModel(String name, String symbol, String code) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
    }
}

