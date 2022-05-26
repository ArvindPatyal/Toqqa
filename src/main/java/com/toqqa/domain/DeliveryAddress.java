package com.toqqa.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class DeliveryAddress {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	private String city;

	private String postCode;

	private String state;

	private String country;

	private String address;

	private String phoneNumber;

	private String latitude;

	private String longitude;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
