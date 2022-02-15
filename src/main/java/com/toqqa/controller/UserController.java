package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.UserBo;
import com.toqqa.payload.Response;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;
	
	@ApiOperation(value = "Returns User data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "sucess"),
			@ApiResponse(code = 400, message = "Bad Request")})			
	@GetMapping("/fetchUser/{id}")
	public Response<UserBo> fetchUser(@PathVariable("id") @Valid String id) {
		return new Response<UserBo>(this.userService.fetchUser(id), "sucess");
	}

	
	@ApiOperation(value = "Returns logged in uses data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "sucess"),
			@ApiResponse(code = 400, message = "Bad Request")})	
	@GetMapping("/currentUser")
	public Response<UserBo> currentUser() {
		return new Response<UserBo>(new UserBo(this.authenticationService.currentUser()), "sucess");
	}
}
