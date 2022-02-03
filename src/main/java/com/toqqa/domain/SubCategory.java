package com.toqqa.domain;

import java.util.List;

import javax.persistence.*;

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

	@ManyToMany
	@JoinTable(name = "sub_category_sme",joinColumns = @JoinColumn(name = "subCat_id"),
			inverseJoinColumns = @JoinColumn(name = "sme_id"))
	private List<Sme> smes;
}