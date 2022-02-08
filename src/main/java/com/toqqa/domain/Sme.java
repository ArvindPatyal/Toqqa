package com.toqqa.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sme")
@Getter
@Setter
public class Sme {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	private String nameOfBusiness;
	private String shopNumber;
	private String street;
	private String city;
	private String state;
	private String country;
	private String businessLogo;

	@ManyToMany(mappedBy = "smes")
	private List<Category> businessCatagory;

	@ManyToMany(mappedBy = "smes")
	private List<SubCategory> businessSubCatagory;
	private String description;
	private boolean isDeleted;
	private String typeOfBusiness;
	private boolean isDeliverToCustomer;
	private double deliveryCharges;
	private boolean isRegisterWithGovt;

}