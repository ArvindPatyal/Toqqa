package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUp {
	@NotNull
	@NotBlank
	private String firstName;

	@NotNull
	@NotBlank
	private String lastName;

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
}
