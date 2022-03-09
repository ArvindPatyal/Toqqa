package com.toqqa.bo;

import java.util.Date;
import java.util.List;

import com.toqqa.domain.OrderInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoBo {
	private String id;
	private Date createdDate;
	private String orderStatus;
	private List<OrderItemBo> orderItemBo;
	private Double amount;
	

	public OrderInfoBo(OrderInfo orderInfo, List<OrderItemBo> orderItemBo) {

		this.id = orderInfo.getId();
		this.createdDate = orderInfo.getCreatedDate();
		this.orderStatus=orderInfo.getOrderStatus();
		this.amount=orderInfo.getAmount();
		this.orderItemBo =  orderItemBo;
		
	}

}
