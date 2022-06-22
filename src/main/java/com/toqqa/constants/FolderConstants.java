package com.toqqa.constants;

public enum FolderConstants {

    DOCUMENTS("documents"), LOGO("logo"), PRODUCTS("products"), BANNER("banner"), INVOICE("invoice"), PROFILE_PICTURE("profile_picture");

    private final String value;

    FolderConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;

    }
}