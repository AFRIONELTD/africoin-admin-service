package com.afrione.africoinservice.usecases.models.admin;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class PortalUserModel {
    private String fullName;
    private String email;
    private boolean twoFAEnabled;
    private String twoFAMethod;
    private boolean requirePasswordChange;
    private List<String> roles;
}
