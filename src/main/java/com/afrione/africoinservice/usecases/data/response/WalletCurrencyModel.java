package com.afrione.africoinservice.usecases.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Created by jnwanya on Sun, 31 Aug, 2025 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletCurrencyModel {
  private String displayName;
  private String symbol;
  private String code;
  private String logoUrl;
  private boolean crypto;
}
