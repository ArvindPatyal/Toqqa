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

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name="user_info")
@Data
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
	
	/*private String houseNumber;
	
	private String street;*/

	@NotNull
	private String password;

	private String address;

	private String city;
	
	private String postCode;
	
	private String state;
	
	private String country;
		
	private String agentId;
	
	private boolean isDeleted;
	
	@ManyToMany(mappedBy = "users")
	private List<Role> roles = new ArrayList<>();
		
}

