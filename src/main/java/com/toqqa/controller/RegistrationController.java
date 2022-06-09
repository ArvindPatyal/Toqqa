package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.payload.AgentRegistrationPayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.SmeRegistrationPayload;
import com.toqqa.payload.UserSignUp;
import com.toqqa.service.RegistrationService;
import com.toqqa.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/registration")
public class RegistrationController {

	@Autowired
	private UserService userService;

	@Autowired
	private RegistrationService registrationService;

	@ApiOperation(value = "User Registration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/customer")
	public Response<?> addUser(@RequestBody @Valid UserSignUp userSignUp) {
		log.info("Invoked:: RegistrationController:: addUser");
		return new Response<>(this.userService.addUser(userSignUp), "success");
	}

	@ApiOperation(value = "Sme registration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/sme")
	public Response<?> smeRegistration(@Valid @ModelAttribute SmeRegistrationPayload smeRegistration) {
		log.info("Invoked:: RegistrationController:: smeRegistration");
		return this.registrationService.registerSme(smeRegistration);
	}

	@ApiOperation(value = "agent registration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/agent")
	public Response<?> agentRegistration(@ModelAttribute @Valid AgentRegistrationPayload agentRegistration) {
		log.info("Invoked:: RegistrationController:: agentRegistration");
		return this.registrationService.registerAgent(agentRegistration);
	}
}
