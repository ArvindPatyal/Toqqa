package com.toqqa.constants;

public enum FileType {
	
	PRODUCT_IMAGE("PRODUCT_IMAGE"),DOCUMENTS("DOCUMENTS"),SHOP_LOGO("SHOP_LOGO");
	
	private final String value;

	private FileType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
	
}
