package com.toqqa.controller;

import com.toqqa.config.JWTConfig;
import com.toqqa.dto.OtpDto;
import com.toqqa.dto.ResetPasswordDto;
import com.toqqa.payload.*;
import com.toqqa.service.OtpService;
import com.toqqa.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTConfig jwtConfig;
    @Autowired
    private OtpService otpService;

    @ApiOperation(value = "Authenticate user and signin")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/signIn")
    public Response<?> signIn(@RequestBody @Valid LoginRequest request) {
        log.info("Invoked:: AuthController:: signIn");
        return new Response<LoginResponse>(this.userService.signIn(request), "success");
    }

    @PostAuthorize(value = "hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "SignIn for Admin")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/adminSignIn")
    public Response adminSignIn(@RequestBody @Valid LoginRequestAdmin request) {
        log.info("Invoked:: AuthController:: signIn");
        return this.userService.adminSignIn(request);
    }


    @ApiOperation(value = "Generates a token for reset password and send it to email")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequestMapping(method = RequestMethod.POST, value = "/reset")
    public Response forgotPassword(@RequestBody @Valid ForgotPasswordDto forgotPasswordDto) {
        log.info("Invoked -+- ResetPasswordController -+- forgotPassword()");
        return this.userService.resetToken(forgotPasswordDto.getEmail());
    }

    @ApiOperation(value = "changes password and validates reset password token")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequestMapping(method = RequestMethod.PUT, value = "/reset")
    public Response resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) {
        log.info("Invoked -+- ResetPasswordController -+- forgotPassword()");
        return this.userService.resetPassword(resetPasswordDto);
    }

    @ApiOperation("Send Otp for verification")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequestMapping(value = "/otp", method = RequestMethod.POST)
    public Response sendOtp(@RequestBody @Valid OtpDto otpDto) {
        log.info("Invoked -+- OtpController -+- sendOtp");
        return this.otpService.sendOtp(otpDto);
    }

}