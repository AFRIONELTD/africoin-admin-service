package com.afrione.africoinservice.usecases.data.response.otc;


import com.afrione.africoinservice.usecases.data.response.WalletCurrencyModel;
import com.afrione.africoinservice.usecases.data.response.merchant.CountryModel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CoinPayoutTransactionResponse {



    private Long id;

    private BigDecimal exchangeRate;

    private String customerName;

    private CountryModel country;

    private CountryModel recipientCountry;

    private BigDecimal fiatAmount;

    private BigDecimal afriAmount;

    private String debitStatus;

    private String creditStatus;

    private FiatCurrencyModel currency;

    private WalletCurrencyModel cryptoCurrency;

    private String recipientBank;

    private String recipientAccountNumber;

    private String recipientAccountName;

}
