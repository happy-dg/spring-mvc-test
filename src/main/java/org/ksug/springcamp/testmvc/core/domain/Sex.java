package org.ksug.springcamp.testmvc.core.domain;

import org.ksug.springcamp.testmvc.core.common.lang.CodeEncodableEnum;

public enum Sex implements CodeEncodableEnum {
    MALE("male", "남성"), FEMALE("female", "여성");

    private String code;
    private String description;

    private Sex(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
