package com.afrione.africoinservice.usecases.models.admin;

import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.RoleEntity;
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

    public static PortalUserModel toModel(AppUserEntity appUserEntity){
       return PortalUserModel
                .builder()
                .fullName(appUserEntity.getFirstName() + " " + appUserEntity.getLastName())
                .email(appUserEntity.getEmail())
                .twoFAEnabled(appUserEntity.isTwoFAEnabled())
                .twoFAMethod(appUserEntity.getTwoFAMethod() != null ? appUserEntity.getTwoFAMethod() : null)
                .requirePasswordChange(appUserEntity.isRequiresPasswordChange())
                .roles(appUserEntity.getRoles().stream().map(RoleEntity::getRoleName).toList())
                .dateJoined(appUserEntity.getDateCreated().toLocalDate())
                .lastLoginDate(appUserEntity.getLastLoginAt() == null ? null : appUserEntity.getLastLoginAt().toLocalDate())
                .phoneNumber(appUserEntity.getPhoneNumber())
                .build();
    }
}
