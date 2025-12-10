package com.afrione.africoinservice.usecases.data.value_objects;

import java.util.HashMap;
import java.util.Map;


public enum NotificationTypeKP {
    TRANSFER_SUCCESSFUL("transfer.success"),
    TRANSFER_FAILED("transfer.failed"),
    VIRTUAL_ACCOUNT_INFLOW("charge.success"),
    UNKNOWN("unknown");

    private final String type;
    NotificationTypeKP(String type){
        this.type = type;
    }
    public String getType() {
        return type;
    }


    private static final Map<String, NotificationTypeKP> map = new HashMap<>();
    static {
        for (NotificationTypeKP value : NotificationTypeKP.values()) {
            map.put(value.type, value);
        }
    }
    public static NotificationTypeKP getByType(String code) {
        NotificationTypeKP typeConstant = map.get(code);
        return typeConstant == null ? UNKNOWN : typeConstant;
    }
}
