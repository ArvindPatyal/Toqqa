package com.toqqa.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	private String productName;

	private String description;

	private Long unitsInStock;

	private Double pricePerUnit;

	private Double discount;

	private Integer maximumUnitsInOneOrder;

	private Integer minimumUnitsInOneOrder;

	private Date expiryDate;

	private String countryOfOrigin;

	private String manufacturerName;
	
	private Boolean isDeleted;
	
	private Date manufacturingDate;
	
	private String banner;

// To Do Awaiting Feedback Around These Two Fields..

//	private Boolean doYouDelieverInSpecifiedRadius;

//	private Boolean delieveredOutsideOfDelieveryRadius;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany
	@JoinTable(name = "products_categories", inverseJoinColumns = @JoinColumn(name = "product_category_id"), joinColumns = @JoinColumn(name = "product_id"))
	private List<ProductCategory> productCategories;

	@ManyToMany
	@JoinTable(name = "product_subcategories", inverseJoinColumns = @JoinColumn(name = "product_subcategory_id"), joinColumns = @JoinColumn(name = "product_id"))
	private List<ProductSubCategory> productSubCategories;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)    
	private List<Attachment> attachments;

}
