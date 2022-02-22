package com.toqqa.service;

import com.toqqa.bo.AgentBo;
import com.toqqa.payload.AgentRegistration;

public interface AgentService {

	AgentBo agentRegistration(AgentRegistration agentRegistration,String userId);
}
