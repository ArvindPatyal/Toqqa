package com.toqqa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class CartItem {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@NotNull
	private Double amount;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@NotNull
	private Integer quantity;


	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

}
