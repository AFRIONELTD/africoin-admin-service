package com.afrione.africoinservice.usecases.data.response.otc;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Created by jnwanya on Sun, 31 Aug, 2025 */
@Data
@NoArgsConstructor
public class WalletModel {

  private String walletId;
  private BigDecimal walletBalance;
  private String currency;
  private String walletAddress;
  private BigDecimal coinBalance = BigDecimal.ZERO;
  private BigDecimal fiatBalance = BigDecimal.ZERO;


}
