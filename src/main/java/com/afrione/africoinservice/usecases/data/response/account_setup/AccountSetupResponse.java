package com.afrione.africoinservice.usecases.data.response.account_setup;

import lombok.Builder;
import lombok.Value;

/**
 * Created by felixadewale on
 * 08/12/2025
 */
@Value @Builder
public class AccountSetupResponse {
     String firstName;
     String lastName;
     String gender;
     String phoneNumber;
     String email;
     String defaultPassword;
}
