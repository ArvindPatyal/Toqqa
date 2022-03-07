package com.toqqa.payload;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class OrderPayload {

	@NotEmpty
	private List<OrderItemPayload> items;

	@NotNull
	private Double amount;

}
