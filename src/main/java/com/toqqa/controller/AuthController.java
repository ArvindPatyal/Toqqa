package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.UserBo;
import com.toqqa.payload.LoginRequest;
import com.toqqa.payload.Response;
import com.toqqa.payload.UserSignUp;
import com.toqqa.service.UserService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/signIn")
	public Response signIn(@RequestBody @Valid LoginRequest request) {
		return new Response(this.userService.signIn(request), "");
	}

	@PostMapping("/signUp")
	public UserBo addUser(@RequestBody @Valid UserSignUp userSignUp) {
		return this.userService.addUser(userSignUp);
	}
}
