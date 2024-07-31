package org.delivery.db.userordermenu.enums;

public enum UserOrderMenuStatus {
    REGISTERED("registered"),
    UNREGISTERED("unregistered");

    UserOrderMenuStatus(String description){
        this.description = description;
    }
    private String description;
}
