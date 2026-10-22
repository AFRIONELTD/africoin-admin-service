package com.afrione.africoinservice.usecases.data.response.otc;


import com.afrione.africoinservice.usecases.data.response.WalletCurrencyModel;
import com.afrione.africoinservice.usecases.data.response.merchant.CountryModel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CoinFundingTransactionResponse {

    private Long id;

    private BigDecimal exchangeRate;

    private String customerName;

    private CountryModel country;

    private BigDecimal afriAmount;

    private BigDecimal fiatAmount;

    private String paymentStatus;

    private String fundingStatus;

    private String dateCreated;

    private FiatCurrencyModel currency;

    private String bankName;

    private String accountName;

    private String accountNumber;

    private  String customerEmail;

    private WalletCurrencyModel cryptoCurrency;

    private BigDecimal networkFee;


}
