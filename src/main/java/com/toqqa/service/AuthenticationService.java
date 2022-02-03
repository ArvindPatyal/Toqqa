package com.toqqa.service;

import com.toqqa.domain.User;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
	Authentication getAuthentication();

	User currentUser();

	Boolean isCustomer();

	Boolean isSME();

	Boolean isAgent();
}
