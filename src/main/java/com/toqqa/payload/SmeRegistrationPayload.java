package com.toqqa.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SmeRegistrationPayload {

	@NotNull
	private UserSignUp userSignUp;
	@Valid
	private SmeRegistration smeRegistration;
	

}
