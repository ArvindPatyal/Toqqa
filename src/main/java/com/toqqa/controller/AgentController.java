package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.AgentBo;
import com.toqqa.payload.AgentUpdate;
import com.toqqa.payload.Response;
import com.toqqa.service.AgentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

	@ApiOperation(value = "agent updation")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/agentupdate")
	public Response<AgentBo> agentUpdate(@ModelAttribute @Valid AgentUpdate agentUpdate) {
		log.info("Inside controller agent Update");
		return new Response<AgentBo>(this.agentService.agentUpdate(agentUpdate), "success");
	}

	@ApiOperation(value = "Returns Agent data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchAgent/{id}")
	public Response<AgentBo> fetchAgent(@PathVariable("id") @Valid String id) {
		log.info("Inside controller fetch user");
		return new Response<AgentBo>(this.agentService.fetchAgent(id), "success");
	}
}
