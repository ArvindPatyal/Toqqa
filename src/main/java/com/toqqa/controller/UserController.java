package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toqqa.bo.UserBo;
import com.toqqa.dto.PushNotificationRequestDto;
import com.toqqa.dto.PushNotificationResponseDto;
import com.toqqa.payload.Response;
import com.toqqa.payload.UpdateUser;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.UserService;
import com.toqqa.service.impls.PushNotificationService;

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
	

	@Autowired
	private PushNotificationService pushNotificationService;

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
	public Response<UserBo> updateUser(@RequestBody @Valid UpdateUser updateUser) {
		log.info("Invoked:: UserController:: updateUser");
		return new Response<UserBo>(this.userService.updateUser(updateUser), "success");
	}
	
	@ApiOperation(value = "noti User")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/notification/token")
    public void sendTokenNotification(@RequestBody PushNotificationRequestDto request) {
		System.out.println("reqqq"+request);
		request.setMessage("First notification");
		request.setTitle("Update");
		request.setToken("eHaKM0EiQNGxtOTrS6HnZE:APA91bEaEP64Lm0NKrumlestf8JcWS77HyFU_cdivHAn2g3mH-CHm4C1a3F6UY51bAbFcaYLe_4BM2huDBAhrdLd9Bnk02nJV7GdasCob0qKJFvU4QUMyMYEhtFqOXs72zxR5LumM-He");
        pushNotificationService.sendPushNotificationToToken(request);
		//return new Response<UserBo>("", "success");

       
       // return new ResponseEntity<>(new PushNotificationResponseDto(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }
}
