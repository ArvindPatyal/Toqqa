package com.toqqa.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.User;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.UserService;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserService userService;

	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public User currentUser() {
		UserDetails userDetail = (UserDetails) getAuthentication().getPrincipal();
		return userService.findByEmailOrPhone(userDetail.getUsername());
	}

	@Override
	public Boolean isCustomer() {
		User user = this.currentUser();
		if (user.getRoles().get(0).getRole().equalsIgnoreCase(RoleConstants.CUSTOMER.getValue())) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean isSME() {
		User user = this.currentUser();
		if (user.getRoles().get(0).getRole().equalsIgnoreCase(RoleConstants.SME.getValue())) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean isAgent() {
		User user = this.currentUser();
		if (user.getRoles().get(0).getRole().equalsIgnoreCase(RoleConstants.AGENT.getValue())) {
			return true;
		}
		return false;
	}

}
