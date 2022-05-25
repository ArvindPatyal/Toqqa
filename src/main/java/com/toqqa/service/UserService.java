package com.toqqa.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.toqqa.bo.UserBo;
import com.toqqa.domain.User;
import com.toqqa.payload.LoginRequest;
import com.toqqa.payload.LoginResponse;
import com.toqqa.payload.UpdateUser;
import com.toqqa.payload.UserSignUp;

public interface UserService extends UserDetailsService {
	UserBo addUser(UserSignUp userSignUp);

	Boolean isUserExists(String email, String phone);

	User findByEmailOrPhone(String userName);

	LoginResponse signIn(LoginRequest bo);

	UserBo fetchUser(String id);

	UserBo updateUser(UpdateUser updateUser);

}
