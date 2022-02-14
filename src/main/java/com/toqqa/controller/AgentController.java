package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.AgentBo;
import com.toqqa.payload.AgentSignUp;
import com.toqqa.service.AgentService;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

	// Map<String, String[]> parameterMap = request.getParameterMap();

	@PostMapping("/addAgent")
	public AgentBo addAgent(@RequestBody @Valid AgentSignUp agentSignUp) {
		return this.agentService.addAgent(agentSignUp);
	}
}
