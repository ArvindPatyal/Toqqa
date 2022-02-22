package com.toqqa.controller;

import com.toqqa.bo.AgentBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.bo.UserBo;
import com.toqqa.payload.*;
import com.toqqa.service.AgentService;
import com.toqqa.service.RegistrationService;
import com.toqqa.service.SmeService;
import com.toqqa.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
		return new Response<>(this.userService.addUser(userSignUp), "success");
	}

	@ApiOperation(value = "Sme registration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/sme")
	public Response<?> smeRegistration(@ModelAttribute @Valid SmeRegistrationPayload smeRegistration) {
		return this.registrationService.registerSme(smeRegistration);
	}

	@ApiOperation(value = "agent registration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/agent")
	public Response<?> agentRegistration(@ModelAttribute @Valid AgentRegistrationPayload agentRegistration) {
		return this.registrationService.registerAgent(agentRegistration);
	}
}
