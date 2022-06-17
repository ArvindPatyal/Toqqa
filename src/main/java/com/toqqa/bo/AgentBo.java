package com.toqqa.bo;

import com.toqqa.domain.Agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentBo {

	private String id;

	private String agentDocuments;

	private String idProof;

	private String userId;

	private String agentId;

	public AgentBo(Agent agent) {
		this.id = agent.getId();
		this.agentDocuments = agent.getAgentDocuments();
		this.idProof = agent.getIdProof();
		this.userId = agent.getUserId();
		this.agentId = agent.getAgentId() != null ? agent.getAgentId() : "";
	}

}
