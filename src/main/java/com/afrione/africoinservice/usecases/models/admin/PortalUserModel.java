package com.afrione.africoinservice.usecases.models.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class PortalUserModel {
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean twoFAEnabled;
    private String twoFAMethod;
    private boolean requirePasswordChange;
    private List<String> roles;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateJoined;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastLoginDate;
}
