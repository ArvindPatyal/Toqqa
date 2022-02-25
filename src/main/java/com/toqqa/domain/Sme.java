package com.toqqa.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
	private String businessAddress;
	private String city;
	private String state;
	private String country;
	private String businessLogo;

	@ManyToMany
	@JoinTable(name = "category_sme", joinColumns = @JoinColumn(name = "sme_id"), inverseJoinColumns = @JoinColumn(name = "cat_id"))
	private List<Category> businessCatagory;

	@ManyToMany
	@JoinTable(name = "sub_category_sme", joinColumns = @JoinColumn(name = "sme_id"), inverseJoinColumns = @JoinColumn(name = "subCat_id"))
	private List<SubCategory> businessSubCatagory;
	private String description;
	private Boolean isDeleted;
	private String typeOfBusiness;
	private Boolean isDeliverToCustomer;
	private Double deliveryRadius;
	private Double deliveryCharges;
	private Boolean isRegisterWithGovt;
	private Date startTimeOfDelivery;
	private Date endTimeOfDelivery;
	private String regDoc;
	private String idProof;
	private String userId;

}