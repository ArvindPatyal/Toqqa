package com.toqqa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "order_item")
public class OrderItem {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	private Integer quantity;
	private Double price;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private OrderInfo orderInfo;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	

}
