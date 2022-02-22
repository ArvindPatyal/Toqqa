package com.toqqa.payload;

import com.toqqa.bo.AgentBo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgentRegistrationResponse extends RegistrationResponse {
	private AgentBo agent;
}
