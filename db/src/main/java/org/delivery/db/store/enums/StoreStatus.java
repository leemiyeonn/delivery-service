package org.delivery.db.store.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StoreStatus {
    REGISTERED("registered"),
    UNREGISTERED("unregistered");

    private String description;
}
