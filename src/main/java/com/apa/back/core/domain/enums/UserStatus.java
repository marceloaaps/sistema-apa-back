package com.apa.back.core.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    APPROVED("APPROVED"),
    PENDING("PENDING"),
    REJECTED("REJECTED");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
