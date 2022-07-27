package com.toqqa.service;

import com.toqqa.dto.ResetPasswordDto;
import com.toqqa.payload.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.toqqa.bo.UserBo;
import com.toqqa.domain.User;

public interface UserService extends UserDetailsService {
	UserBo addUser(UserSignUp userSignUp);

	Boolean isUserExists(String email, String phone);

	User findByEmailOrPhone(String userName);

	LoginResponse signIn(LoginRequest bo);

	UserBo fetchUser(String id);

	UserBo updateUser(UpdateUser updateUser);
	
	User getById(String id);

	Response resetToken(String email);
	Response resetPassword(ResetPasswordDto resetPasswordDto);

	ResponseEntity adminSignIn(LoginRequestAdmin request);

	Response userFromToken(String token);
}
