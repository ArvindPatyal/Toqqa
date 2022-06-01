package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.toqqa.constants.OrderConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdatePayload {

	@NotNull
	@NotBlank
	private String orderId;
	@NotNull
	@NotBlank
	private OrderConstants orderConstant;
}
