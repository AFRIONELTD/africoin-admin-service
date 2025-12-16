package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.request.LoginRequest;
import com.afrione.africoinservice.usecases.data.request.ChangePasswordRequest;
import com.afrione.africoinservice.usecases.data.request.Toggle2FARequest;
import com.afrione.africoinservice.usecases.data.response.login.LoginResponse;
import com.afrione.africoinservice.usecases.data.response.auth.Toggle2FAResponse;

/**
 * Created by felixadewale on
 * 08/12/2025
 */
public interface AuthUseCases {
    String login(LoginRequest request);
    void changePassword(ChangePasswordRequest request, Long userId);
    Toggle2FAResponse enable2FA(Toggle2FARequest request, Long userId);
    Toggle2FAResponse disable2FA(Long userId);
    LoginResponse completeLogin(String sessionId, String token);
}
