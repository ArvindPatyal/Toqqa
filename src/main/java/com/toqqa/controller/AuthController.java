package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.payload.LoginRequest;
import com.toqqa.payload.LoginResponse;
import com.toqqa.payload.Response;
import com.toqqa.service.SmeService;
import com.toqqa.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private SmeService smeService;

	@ApiOperation(value = "Authenticate user and signin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/signIn")
	public Response<?> signIn(@RequestBody @Valid LoginRequest request) {
		return new Response<LoginResponse>(this.userService.signIn(request), "success");
	}

}
