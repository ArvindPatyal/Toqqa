package com.toqqa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name="user_info")
public class User {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")	
	private String id;
	
	@Size(max = 1024)
	@NotNull
	@NotBlank
	private String firstName;
	
	@Size(max = 1024)
	@NotNull
	@NotBlank
	private String lastName;
	
	@Column(unique = true,nullable = true)
	private String email;
	
	@NotNull
	@Column(unique = true)	
	private String phone;				
	
	private String houseNumber;
	
	private String street;
	
	private String city;
	
	private String postCode;
	
	private String state;
	
	private String country;
		
	private String agentId;
	
	private boolean isDeleted;
	
	@ManyToMany(mappedBy = "users")
	private final List<Role> roles = new ArrayList<>();
		
}

