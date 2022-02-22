package com.toqqa.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgentRegistrationPayload {

	private UserSignUp userSignUp;

	private AgentRegistration agentRegistration;

}
