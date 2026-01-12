package com.afrione.africoinservice.usecases.data.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Toggle2FARequest {

    @NotEmpty(message = "2FA method cannot be empty")
    private String twoFAMethod; // SMS, EMAIL, AUTHENTICATOR

}
