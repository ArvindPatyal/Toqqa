package com.toqqa.constants;

public enum OrderConstants {

	ORDER_RECEIVED("received"), READY_TO_SHIP("ready_to_ship"), ORDER_PLACED("placed");

	private final String value;

	OrderConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;

	}

}
