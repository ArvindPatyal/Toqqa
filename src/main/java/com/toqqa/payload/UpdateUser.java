package com.toqqa.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateUser {

	@NotNull
	@NotBlank
	private String firstName;

	@NotNull
	@NotBlank
	private String lastName;

	@Email
	private String email;

	private String phone;

	@NotNull
	private String address;

	@NotNull
	@NotBlank
	private String city;

	@NotNull
	@NotBlank
	private String postCode;

	@NotNull
	@NotBlank
	private String state;

	@NotNull
	@NotBlank
	private String country;

	private String agentId;

	@NotNull
	@NotBlank
	private String password;

	@NotNull
	private String userId;
}
