package com.toqqa.constants;

public enum FolderConstants {

	DOCUMENTS("documents"), LOGO("logo"), PRODUCTS("products");

	private final String value;

	FolderConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;

	}
}