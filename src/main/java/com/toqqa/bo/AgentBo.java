package com.toqqa.bo;

import com.toqqa.domain.Agent;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgentBo {

	private String id;

	private String agentDocuments;

	private String idProof;

	private String userId;

	public AgentBo(Agent agent) {
		this.id = agent.getId();
		this.agentDocuments = agent.getAgentDocuments();
		this.idProof = agent.getIdProof();
		this.userId = agent.getUserId();
	}
}
