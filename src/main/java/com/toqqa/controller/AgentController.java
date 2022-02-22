package com.toqqa.controller;

import javax.validation.Valid;

import com.toqqa.payload.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.AgentBo;
import com.toqqa.payload.AgentRegistration;
import com.toqqa.service.AgentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

}
