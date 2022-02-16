package com.toqqa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

	@Column(unique = true, nullable = true)
	private String email;

	@Column(unique = true, nullable = true)
	private String phone;

	/*
	 * private String houseNumber;
	 * 
	 * private String street;
	 */

	@NotNull
	private String password;

	private String address;

	private String city;

	private String postCode;

	private String state;

	private String country;

	private String agentId;

	private Boolean isDeleted;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles",joinColumns= @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles=new ArrayList<Role>();

}
