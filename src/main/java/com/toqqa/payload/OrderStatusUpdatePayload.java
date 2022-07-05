package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.toqqa.constants.OrderStatus;

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

	private OrderStatus orderStatus;
}
