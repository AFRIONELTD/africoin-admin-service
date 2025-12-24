package com.afrione.africoinservice.usecases.data.response.customer;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Created by felixadewale on
 * 15/12/2025
 */
@Value
@Builder
public class AppUserModel {
    Long id;
    String firstName;
    String lastName;
    String email;
    String gender;
    String phoneNumber;
    String dateOfBirth;
    String dateJoined;
    boolean passwordUpdateRequired;
    String verificationStatus;
    List<FileModel> files;
}
