package com.afrione.africoinservice.usecases.data.response.otc;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;
    private String firstName;
    private String lastName;
    private String userId;
    private String email;
    private String gender;
    private String phoneNumber;
    private String idType;
    private String idNumber;
    private String dateOfBirth;
    private String dateJoined;
    private boolean passwordUpdateRequired;
    private String verificationStatus;
    private List<FileModel> files;
}
