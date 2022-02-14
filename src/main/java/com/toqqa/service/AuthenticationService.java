package com.toqqa.service;

import org.springframework.security.core.Authentication;

import com.toqqa.domain.User;

public interface AuthenticationService {
	Authentication getAuthentication();

	User currentUser();

	Boolean isCustomer();

	Boolean isSME();

	Boolean isAgent();
}
