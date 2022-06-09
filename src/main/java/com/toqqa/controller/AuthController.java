package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.config.JWTConfig;
import com.toqqa.payload.LoginRequest;
import com.toqqa.payload.LoginResponse;
import com.toqqa.payload.Response;
import com.toqqa.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	@Autowired
	private JWTConfig jwtConfig;

	@ApiOperation(value = "Authenticate user and signin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/signIn")
	public Response<?> signIn(@RequestBody @Valid LoginRequest request) {
		log.info("Invoked:: AuthController:: signIn");
		return new Response<LoginResponse>(this.userService.signIn(request), "success");
	}

	// TODO generate refresh token

//	@RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
//	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
//
//		String token = "";
//
//		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String existingToken = getJwtToken(request);
//		token = jwtConfig.getRefreshToken(existingToken, userDetails);
//
//		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
//	}
//
//	private String getJwtToken(HttpServletRequest request) {
//		String autherizationHeader = request.getHeader("Authorization");
//		return autherizationHeader.substring(7);
//	}

}