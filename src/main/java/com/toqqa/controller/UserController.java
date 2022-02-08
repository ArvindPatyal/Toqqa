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

@RestController
@RequestMapping("api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping("/fetchUser/{id}")
	public Response fetchUser(@PathVariable("id") @Valid String id) {
		return new Response(this.userService.fetchUser(id), "");
	}

	@GetMapping("/currentUser")
	public Response currentUser() {
		return new Response(new UserBo(this.authenticationService.currentUser()), "");
	}
}
