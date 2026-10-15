package com.afrione.africoinservice.usecases.data.response.otc;


import com.afrione.africoinservice.usecases.data.response.WalletCurrencyModel;
import com.afrione.africoinservice.usecases.data.response.merchant.CountryModel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CoinTransferTransactionResponse {

    private Long id;

    private BigDecimal exchangeRate;

    private String customerName;

    private String senderAddress;

    private String recipientName;

    private CountryModel country;

    private BigDecimal afriAmount;

    private String status;

    private WalletCurrencyModel cryptoCurrency;

    private String recipientAddress;

    private String dateCreated;


}
