package com.toqqa.service;

import com.toqqa.dto.AdminRegistrationDto;
import com.toqqa.payload.AgentRegistrationPayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.SmeRegistrationPayload;
import org.springframework.http.ResponseEntity;

public interface RegistrationService {

	Response registerAgent(AgentRegistrationPayload payload);

	Response registerSme(SmeRegistrationPayload payload);

	ResponseEntity adminRegistration(AdminRegistrationDto adminRegistrationDto);
}
