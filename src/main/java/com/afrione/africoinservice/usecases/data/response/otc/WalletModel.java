package com.afrione.africoinservice.usecases.data.response.otc;


import com.afrione.africoinservice.usecases.data.response.WalletCurrencyModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Created by jnwanya on Sun, 31 Aug, 2025 */
@Data
@NoArgsConstructor
public class WalletModel {

  private String walletId;
  private BigDecimal walletBalance;
  private WalletCurrencyModel currency;
  private String walletAddress;
  private BigDecimal coinBalance = BigDecimal.ZERO;
  private FiatCurrencyModel fiatCurrency;
  private BigDecimal fiatBalance = BigDecimal.ZERO;


}
