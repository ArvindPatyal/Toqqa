package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdatePayload {

	@NotNull
	private Boolean isCancelled;

	@NotNull
	@NotBlank
	private String orderId;

}
