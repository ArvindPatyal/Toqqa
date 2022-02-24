package com.toqqa.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "product_category")
@NoArgsConstructor
public class ProductCategory {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")

	private String id;

	private String productCategory;

	@OneToMany(mappedBy = "productCategory")
	private List<ProductSubCategory> productSubCategories;

	@ManyToMany(mappedBy = "productCategories", fetch = FetchType.LAZY)
	private List<Product> products;

}