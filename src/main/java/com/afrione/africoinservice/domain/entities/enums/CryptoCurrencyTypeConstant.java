package com.afrione.africoinservice.domain.entities.enums;

import lombok.Getter;

@Getter
public enum CryptoCurrencyTypeConstant {

    AFRi_ERC20("AFRi", "AFR", "AFRi_ERC20", true, true, "", 6),
    AFRi_TRC20("AFRi(TRC20)", "AFRi_TRC20", "AFRi_TRC20", true, true, "", 6);

    private final String displayName;
    private final String symbol;
    private final String logoUrl;
    private final String code;
    private final boolean token;
    private final boolean cryptoCoin;
    private final int decimalPlaces;

    CryptoCurrencyTypeConstant(String name, String symbol, String code, boolean isToken, boolean cryptoCoin, String logoUrl, int standardDecimalPlaces) {
        this.displayName = name;
        this.symbol = symbol;
        this.code = code;
        this.token = isToken;
        this.cryptoCoin = cryptoCoin;
        this.decimalPlaces = standardDecimalPlaces;
        this.logoUrl = logoUrl;
    }
}


