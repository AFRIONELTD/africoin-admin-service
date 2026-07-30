package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.WalletCurrencyModel;
import com.afrione.africoinservice.usecases.data.response.merchant.CountryModel;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoRateResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.RateStatsModel;
import com.afrione.africoinservice.usecases.data.response.otc.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by felixadewale on
 * 15/12/2025
 */
public interface CustomerReadUseCases extends ExternalRequestUseCases {
    PagedResponse<AppUserModel> listUsers(String searchTerm, String searchStatus, int pageNo, int pageSize, Long accountId);
    UserKycDetailModel getAppUser(String userId, Long accountId);

    RateStatsModel retrieveRateStats(String fiat, Long accountId);

    List<CryptoRateResponse> retrieveRate(Long accountId);

    PagedResponse<AppUserModel> getActiveUsers(String searchKey, String countryCode, int pageNo, int pageSize, AuthenticatedUser authenticatedUser);

    List<WalletModel> getUserWallets(String userId, AuthenticatedUser authenticatedUser);

    PagedResponse<CoinTransactionResponse> getCustomerTransactions(String userId, String transactionType,
                                                                   LocalDate startDate, LocalDate endDate, int pageNo , int pageSize, AuthenticatedUser authenticatedUser);


    PagedResponse<ExchangeRateHistoryResponse> fetchExchangeRateHistory(String fiatCurrency, String cryptoCurrency, LocalDate startDate, LocalDate endDate, int pageNo, int pageSize,
                                                                        AuthenticatedUser authenticatedUser);



    List<PayoutProcessorInfoModel> getSupportedCorridors();

    List<WalletCurrencyModel> getSupportedWalletCurrencies();

    List<CountryModel> getActiveCountries();

}
