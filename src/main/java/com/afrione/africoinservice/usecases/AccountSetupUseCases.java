package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.request.AccountSetupRequest;
import com.afrione.africoinservice.usecases.data.response.account_setup.AccountSetupResponse;
import com.afrione.africoinservice.usecases.data.response.auth.ForgotPasswordResponse;

/**
 * Created by felixadewale on
 * 08/12/2025
 */
public interface AccountSetupUseCases {
    AccountSetupResponse setupAccount(AccountSetupRequest request, Long userId);

    ForgotPasswordResponse initiateForgotPassword(String emailAddress);

    void finaliseForgotPassword(String sessionId, String otp, String newPassword);
}
