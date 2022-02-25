package com.toqqa.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "sub_category")
public class SubCategory {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	private String subcategory;

	@ManyToOne
	@JoinColumn(name = "cat_id")
	private Category category;

	@ManyToMany(mappedBy = "businessSubCatagory")
	private List<Sme> smes;
}
