package com.toqqa.service;

import com.toqqa.payload.AgentRegistrationPayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.SmeRegistrationPayload;

public interface RegistrationService {

	Response registerAgent(AgentRegistrationPayload payload);

	Response registerSme(SmeRegistrationPayload payload);
}
