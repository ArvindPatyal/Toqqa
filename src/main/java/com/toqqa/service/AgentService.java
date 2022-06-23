package com.toqqa.service;

import com.toqqa.bo.AgentBo;
import com.toqqa.payload.AgentPayload;
import com.toqqa.payload.AgentRegistration;
import com.toqqa.payload.AgentUpdate;
import com.toqqa.payload.Response;

public interface AgentService {

    AgentBo agentRegistration(AgentRegistration agentRegistration, String userId);

    AgentBo agentUpdate(AgentUpdate payload);

    AgentBo fetchAgent(String id);

    Response fetchAgentReferralList(AgentPayload agentPayload);

}
