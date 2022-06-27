package com.toqqa.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.toqqa.constants.OrderStatus;
import com.toqqa.constants.PaymentConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_info")
public class OrderInfo {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(unique = true)
	private String orderTransactionId;

	@CreationTimestamp
	private Date createdDate;

	@UpdateTimestamp
	private Date modificationDate;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<OrderItem> orderItems;

	private Double amount;

	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private String invoiceNumber;

	@Column(length = 500)
	private String cancellationReason;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private double shippingFee;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deliveryAddress_id")
	private DeliveryAddress address;

	@Enumerated(EnumType.STRING)
	private PaymentConstants paymentType;

	@ManyToOne
	@JoinColumn(name = "sme_id")
	private Sme sme;

}
