package com.toqqa.controller;

import javax.validation.Valid;

import com.toqqa.payload.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.AgentBo;
import com.toqqa.payload.AgentSignUp;
import com.toqqa.service.AgentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

	// Map<String, String[]> parameterMap = request.getParameterMap();
	@ApiOperation(value = "Add Agent")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/addAgent")
	public Response<AgentBo> addAgent(@ModelAttribute @Valid AgentSignUp agentSignUp) {
		return new Response<AgentBo>(this.agentService.addAgent(agentSignUp),"success");
	}
}
