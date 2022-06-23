package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.toqqa.bo.UserBo;
import com.toqqa.payload.Response;
import com.toqqa.payload.UpdateUser;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;

	@ApiOperation(value = "Returns User data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchUser/{id}")
	public Response<UserBo> fetchUser(@PathVariable("id") @Valid String id) {
		log.info("Invoked:: UserController:: fetchUser");
		return new Response<UserBo>(this.userService.fetchUser(id), "success");
	}

	@ApiOperation(value = "Returns logged in uses data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/currentUser")
	public Response<UserBo> currentUser() {
		log.info("Invoked:: UserController:: currentUser");
		return new Response<UserBo>(new UserBo(this.authenticationService.currentUser()), "success");
	}

	@ApiOperation(value = "Update User")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PutMapping("/updateuser")
	public Response<UserBo> updateUser(@ModelAttribute @Valid UpdateUser updateUser) {
		log.info("Invoked:: UserController:: updateUser");
		return new Response<UserBo>(this.userService.updateUser(updateUser), "success");
	}
}
