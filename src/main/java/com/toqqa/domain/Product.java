package com.toqqa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
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

	@CreationTimestamp
	private Date createdAt;

	private Boolean deliveredInSpecifiedRadius;

	private Boolean delieveredOutsideSpecifiedRadius;

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

	@Column(length = 500)
	private Integer sequenceNumber;

}
