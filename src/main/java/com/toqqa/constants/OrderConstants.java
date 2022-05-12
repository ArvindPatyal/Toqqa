package com.toqqa.constants;

public enum OrderConstants {

	ORDER_RECEIVED("RECEIVED"), READY_TO_SHIP("READY_TO_SHIP"), ORDER_PLACED("PLACED");

	private final String value;

	OrderConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;

	}

}
