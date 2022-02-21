package com.toqqa.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "product_subcategory")
public class ProductSubCategory {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")

	private String id;

	private String productSubCategory;

	@ManyToOne
	@JoinColumn(name = "cat_id")
	private ProductCategory productCategory;

	@ManyToMany(mappedBy = "productSubCategories", fetch = FetchType.LAZY)
	private List<Product> products;

}
