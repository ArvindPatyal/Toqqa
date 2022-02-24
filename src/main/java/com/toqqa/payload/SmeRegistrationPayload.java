package com.toqqa.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SmeRegistrationPayload {

	@NotNull
	private UserSignUp userSignUp;
	@NotNull
	private SmeRegistration smeRegistration;

}
