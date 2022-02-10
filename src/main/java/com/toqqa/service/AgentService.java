package com.toqqa.service;

import com.toqqa.bo.AgentBo;
import com.toqqa.payload.AgentSignUp;

public interface AgentService {

	AgentBo addAgent(AgentSignUp agentSignUp);
}
