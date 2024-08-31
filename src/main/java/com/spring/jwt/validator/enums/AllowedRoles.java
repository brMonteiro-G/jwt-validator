package com.spring.jwt.validator.enums;

public enum AllowedRoles {

    ADMIN("admin"), MEMBER("member"), EXTERNAL("external");

    public String value;

    AllowedRoles(String valueArg) {
        value = valueArg;
    }

}
