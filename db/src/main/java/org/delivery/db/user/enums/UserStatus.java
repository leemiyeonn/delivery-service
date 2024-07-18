package org.delivery.db.user.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatus {
    REGISTERED("registered"),
    UNREGISTERED("unregistered");

    private final String description;
}
