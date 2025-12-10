package com.afrione.africoinservice.domain.entities.enums;


import lombok.Getter;

@Getter
public enum PrivilegeTypeConstant {
    CAN_VIEW_ORDERS("CAN VIEW ORDERS", "Can view orders"),
    CAN_CREATE_ORDERS("CAN CREATE ORDERS", "Can create new orders"),
    CAN_APPROVE_ORDERS("CAN APPROVE ORDERS", "Can approve orders");

    private final String displayName;
    private final String description;
    PrivilegeTypeConstant(String name, String description) {
        this.description = description;
        this.displayName = name;
    }
}
