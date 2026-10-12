package com.afrione.africoinservice.usecases.data.response.otc;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.math.BigDecimal;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmountSummary {

      BigDecimal totalAmount;

     FiatCurrencyModel currency;







}
