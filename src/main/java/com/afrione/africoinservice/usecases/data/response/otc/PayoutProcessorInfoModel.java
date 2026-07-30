package com.afrione.africoinservice.usecases.data.response.otc;

import java.util.List;

public class PayoutProcessorInfoModel {

    private long recordId;
    private List<PayoutProcessorChannelModel> payoutProcessorChannels;
    private String countryName;
    private String countryCode;
    private FiatCurrencyModel fiatCurrency;
}
