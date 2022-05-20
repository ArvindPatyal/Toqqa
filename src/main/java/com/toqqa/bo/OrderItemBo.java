package com.toqqa.bo;

import com.toqqa.domain.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemBo {

	private String id;
	private Integer quantity;
	private Double price;
	private ProductBo product;

	public OrderItemBo(OrderItem orderItem) {

		this.id = orderItem.getId();
		this.quantity = orderItem.getQuantity();
		this.price = orderItem.getPrice();
		this.product = new ProductBo(orderItem.getProduct());

	}

}