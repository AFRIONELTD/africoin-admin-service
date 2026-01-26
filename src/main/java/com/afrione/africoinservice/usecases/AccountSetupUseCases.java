package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.request.AccountSetupRequest;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.account_setup.AccountSetupResponse;
import com.afrione.africoinservice.usecases.data.response.auth.ForgotPasswordResponse;
import com.afrione.africoinservice.usecases.models.admin.PortalUserModel;

import java.util.List;

/**
 * Created by felixadewale on
 * 08/12/2025
 */
public interface AccountSetupUseCases {
    AccountSetupResponse setupAccount(AccountSetupRequest request, Long userId);

    ForgotPasswordResponse initiateForgotPassword(String emailAddress);

    void finaliseForgotPassword(String sessionId, String otp, String newPassword);

    ForgotPasswordResponse resendToken(String sessionId);

    void deleteUser(Long userId, Long userId1);

    PagedResponse<PortalUserModel> getAllUsers(int pageNo, int pageSize, Long accountId);

    void updateAccount(Long userId, AccountSetupRequest request, Long userId1);
}
