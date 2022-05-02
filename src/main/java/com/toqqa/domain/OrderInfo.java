package com.toqqa.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "order_info")
public class OrderInfo {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	private Date createdDate;

	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private Double amount;

	private String orderStatus;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<OrderItem> orderItems;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

//	@Column(name = "deliveryAddress_id")
//	private String deliveryAddressId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deliveryAddress_id")
	private DeliveryAddress address;

}
