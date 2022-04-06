package com.toqqa.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
public class Advertisement {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	private String description;

	@CreationTimestamp
	private Date createdDate;

	@UpdateTimestamp
	private Date modificationDate;

	private Integer clicks;

	private String banner;

	private Boolean isDeleted;

	private Boolean isActive;
	
	private Date queueDate;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
