package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCancelPayload {

	@NotBlank
	@NotNull
	private String orderId;

	@Size(max = 200, message = "Reason for cancelation need to have only 200 characters")
	private String cancelationReason;
}
