package com.toqqa.constants;

public enum RoleConstants {

	CUSTOMER("ROLE_CUSTOMER"), AGENT("ROLE_AGENT"), SME("ROLE_SME");

	private final String value;

	RoleConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
