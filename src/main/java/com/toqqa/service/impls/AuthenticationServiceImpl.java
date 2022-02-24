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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserService userService;

	@Override
	public Authentication getAuthentication() {
		log.info("Inside get authentication");
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public User currentUser() {
		log.info("Inside current user");
		UserDetails userDetail = (UserDetails) getAuthentication().getPrincipal();
		return userService.findByEmailOrPhone(userDetail.getUsername());
	}

	@Override
	public Boolean isCustomer() {
		log.info("Inside is customer");
		User user = this.currentUser();
		if (user.getRoles().get(0).getRole().equalsIgnoreCase(RoleConstants.CUSTOMER.getValue())) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean isSME() {
		log.info("Inside is sme");
		User user = this.currentUser();
		if (user.getRoles().get(0).getRole().equalsIgnoreCase(RoleConstants.SME.getValue())) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean isAgent() {
		log.info("Inside is agent");
		User user = this.currentUser();
		if (user.getRoles().get(0).getRole().equalsIgnoreCase(RoleConstants.AGENT.getValue())) {
			return true;
		}
		return false;
	}

}
