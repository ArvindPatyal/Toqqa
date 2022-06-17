package com.toqqa.payload;

import javax.validation.Valid;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgentRegistrationPayload {

	@Valid
	private UserSignUp userSignUp;

	@Valid
	private AgentRegistration agentRegistration;

}
