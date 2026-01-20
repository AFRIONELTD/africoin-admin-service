package com.afrione.africoinservice.domain.entities.enums;


import lombok.Getter;

@Getter
public enum PrivilegeTypeConstant {
    // Merchant Management
    CAN_VIEW_MERCHANTS("CAN VIEW MERCHANTS", "Can view merchant information"),
    CAN_APPROVE_KYC("CAN APPROVE KYC", "Can approve or decline merchant KYC documents"),

    // Rate Management
    CAN_VIEW_RATES("CAN VIEW RATES", "Can view exchange rates"),
    CAN_UPDATE_RATES("CAN UPDATE RATES", "Can update exchange rates and markup"),

    // User Management
    CAN_VIEW_USERS("CAN VIEW USERS", "Can view user information"),
    CAN_SETUP_ACCOUNTS("CAN SETUP ACCOUNTS", "Can setup user accounts"),

    // Role Management
    CAN_VIEW_ROLES("CAN VIEW ROLES", "Can view roles and privileges"),
    CAN_MANAGE_ROLES("CAN MANAGE ROLES", "Can create, update, and delete roles"),
    CAN_ASSIGN_ROLES("CAN ASSIGN ROLES", "Can assign roles to users"),

    // System Administration
    CAN_VIEW_AUDIT_LOGS("CAN VIEW AUDIT LOGS", "Can view system audit logs"),
    CAN_MANAGE_SYSTEM("CAN MANAGE SYSTEM", "Can manage system configuration");

    private final String displayName;
    private final String description;
    PrivilegeTypeConstant(String name, String description) {
        this.description = description;
        this.displayName = name;
    }
}
