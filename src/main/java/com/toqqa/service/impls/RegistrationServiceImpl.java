package com.toqqa.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toqqa.bo.AgentBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.bo.UserBo;
import com.toqqa.exception.UserAlreadyExists;
import com.toqqa.payload.AgentRegistrationPayload;
import com.toqqa.payload.AgentRegistrationResponse;
import com.toqqa.payload.Response;
import com.toqqa.payload.SmeRegistrationPayload;
import com.toqqa.payload.SmeRegistrationResponse;
import com.toqqa.service.AgentService;
import com.toqqa.service.RegistrationService;
import com.toqqa.service.SmeService;
import com.toqqa.service.UserService;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	@Autowired
	private UserService userService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private SmeService smeService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Response<?> registerAgent(AgentRegistrationPayload payload) {
		if (userService.isUserExists(payload.getUserSignUp().getEmail(), payload.getUserSignUp().getPhone())) {
			throw new UserAlreadyExists("user already exists");
		}
		UserBo user = this.userService.addUser(payload.getUserSignUp());
		AgentBo agentBo = this.agentService.agentRegistration(payload.getAgentRegistration(), user.getId());
		AgentRegistrationResponse registrationResponse = new AgentRegistrationResponse();
		registrationResponse.setUser(this.userService.fetchUser(user.getId()));
		registrationResponse.setAgent(agentBo);
		return new Response<AgentRegistrationResponse>(registrationResponse, "success");
	}

	@Override
	public Response<?> registerSme(SmeRegistrationPayload payload) {
		if (userService.isUserExists(payload.getUserSignUp().getEmail(), payload.getUserSignUp().getPhone())) {
			throw new UserAlreadyExists("user already exists");
		}
		UserBo user = this.userService.addUser(payload.getUserSignUp());
		SmeBo sme = this.smeService.smeRegistration(payload.getSmeRegistration(), user.getId());
		SmeRegistrationResponse registrationResponse = new SmeRegistrationResponse();
		registrationResponse.setUser(this.userService.fetchUser(user.getId()));
		registrationResponse.setSme(sme);
		return new Response<SmeRegistrationResponse>(registrationResponse, "success");
	}
}