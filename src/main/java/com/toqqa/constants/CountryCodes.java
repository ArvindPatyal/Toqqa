package com.toqqa.constants;

public enum CountryCodes {
    INDIA("5454"), PAKISTAN("5456");

    private final String value;

    private CountryCodes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
