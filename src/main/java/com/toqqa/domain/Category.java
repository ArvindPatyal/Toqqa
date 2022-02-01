package com.toqqa.domain;

import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "category")
@NoArgsConstructor
public class Category {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	private String category;

	@OneToMany(mappedBy = "category")
	private List<SubCategory> subCategories;

	@ManyToMany
	@JoinTable(name = "category_sme",joinColumns = @JoinColumn(name = "cat_id"),
			inverseJoinColumns = @JoinColumn(name = "sme_id"))
	private List<Sme> smes;
}
