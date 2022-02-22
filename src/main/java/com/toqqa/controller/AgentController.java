package com.toqqa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.service.AgentService;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

}
