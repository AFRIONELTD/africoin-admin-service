package com.afrione.africoinservice.usecases.data.request;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

/**
 * Created by felixadewale on
 * 08/12/2025
 */

@Builder @Value
public class AccountSetupRequest {
    String firstName;
    String lastName;
    String gender;
    String phoneNumber;
    String email;
    Set<Long> roles;
}
