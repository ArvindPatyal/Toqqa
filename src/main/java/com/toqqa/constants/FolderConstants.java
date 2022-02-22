package com.toqqa.constants;

public enum FolderConstants {

	DOCUMENTS("FOLDER_DOCUMENTS"), LOGO("FOLDER_LOGO"), PRODUCTS("FOLDER_PRODUCTS");

	private final String value;

	FolderConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;

	}
}