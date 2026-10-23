package com.afrione.africoinservice.usecases.feature.customer.transaction;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.usecases.ExternalRequestUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.*;

import java.time.LocalDate;

public interface TransactionUseCases extends ExternalRequestUseCases {

    CoinPayoutTransactionResponse fetchPayoutTransactionDetails(Long id, AuthenticatedUser authenticatedUser);

    AmountSummaryResponse getAfriAndPaymentSummaryForFunding(LocalDate startDate, LocalDate endDate, String countryCode, AuthenticatedUser authenticatedUser);

    AmountSummaryResponse getAfriAndPaymentSummaryForTransfer(LocalDate startDate, LocalDate endDate, String countryCode, AuthenticatedUser authenticatedUser);

    AmountSummaryResponse getAfriAndPaymentSummaryForPayout(LocalDate startDate, LocalDate endDate, String countryCode, AuthenticatedUser authenticatedUser);

    PagedResponse<CoinFundingTransactionResponse> fetchCoinFundingTransactions(LocalDate startDate, LocalDate endDate, String countryCode, int pageNo, int pageSize , AuthenticatedUser authenticatedUser);

    PagedResponse<CoinTransferTransactionResponse> fetchTransferTransactions(LocalDate startDate, LocalDate endDate, String countryCode, int pageNo, int pageSize , AuthenticatedUser authenticatedUser);


    PagedResponse<CoinPayoutTransactionResponse> fetchPayoutTransactions(LocalDate startDate, LocalDate endDate, String countryCode, int pageNo, int pageSize , AuthenticatedUser authenticatedUser);

    CoinFundingTransactionResponse fetchFundTransactionDetails(Long id, AuthenticatedUser authenticatedUser);

    CoinTransferTransactionResponse fetchTransferTransactionDetails(Long id, AuthenticatedUser authenticatedUser);

    ExportResult downloadCoinFundingReport(LocalDate startDate, LocalDate endDate, String countryCode, AuthenticatedUser authenticatedUser);

    ExportResult downloadTransferReport(LocalDate startDate, LocalDate endDate, String countryCode,  AuthenticatedUser authenticatedUser);

    ExportResult downloadPayoutReport(LocalDate startDate, LocalDate endDate, String countryCode, AuthenticatedUser authenticatedUser);
}
