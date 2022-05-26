package com.toqqa.bo;

import com.toqqa.domain.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
	private double shippingFee;

	private String invoiceUrl;

	private SmeBo smeBo;

	public OrderInfoBo(OrderInfo orderInfo, List<OrderItemBo> orderItemBo, SmeBo smeBo) {

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
		this.paymentType = orderInfo.getPaymentType().name();
		this.shippingFee = orderInfo.getShippingFee();
		this.smeBo = smeBo;
	}
}
