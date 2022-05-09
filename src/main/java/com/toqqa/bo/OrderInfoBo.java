package com.toqqa.bo;

import java.util.Date;
import java.util.List;

import com.toqqa.domain.OrderInfo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoBo {
	private String id;
	private Date createdDate;
	private String orderStatus;
	private List<OrderItemBo> orderItemBo;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private Double amount;
	private DeliveryAddressBo address;
	private String paymentType;

	public OrderInfoBo(OrderInfo orderInfo, List<OrderItemBo> orderItemBo) {

		this.id = orderInfo.getId();
		this.createdDate = orderInfo.getCreatedDate();
		this.orderStatus = orderInfo.getOrderStatus();
		this.amount = orderInfo.getAmount();
		this.orderItemBo = orderItemBo;
		this.email = orderInfo.getEmail();
		this.phone = orderInfo.getPhone();
		this.firstName = orderInfo.getFirstName();
		this.lastName = orderInfo.getLastName();
		this.address = new DeliveryAddressBo(orderInfo.getAddress());
		this.paymentType=orderInfo.getPaymentType().name();
	}
}
