package com.toqqa.service.impls;

import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.User;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserService userService;

	@Override
	public Authentication getAuthentication() {
		log.info("Invoked :: AuthenticationServiceImpl :: getAuthentication()");
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public User currentUser() {
		log.info("Invoked :: AuthenticationServiceImpl :: currentUser()");
		UserDetails userDetail = (UserDetails) getAuthentication().getPrincipal();
		return userService.findByEmailOrPhone(userDetail.getUsername());
	}

	@Override
	public Boolean isCustomer() {
		log.info("Invoked :: AuthenticationServiceImpl :: isCustomer()");
		User user = this.currentUser();
		return user.getRoles().stream().anyMatch(role -> role.getRole().equalsIgnoreCase(RoleConstants.CUSTOMER.getValue()));
	}

	@Override
	public Boolean isAdmin() {
		log.info("Invoked :: AuthenticationServiceImpl :: isAdmin()");
		User user = this.currentUser();
		return user.getRoles().stream().anyMatch(role -> role.getRole().equalsIgnoreCase(RoleConstants.ADMIN.getValue()));
	}

	@Override
	public Boolean isSME() {
		log.info("Invoked :: AuthenticationServiceImpl :: isSMe()");
		User user = this.currentUser();
		return user.getRoles().stream().anyMatch(role -> role.getRole().equalsIgnoreCase(RoleConstants.SME.getValue()));
	}

	@Override
	public Boolean isAgent() {
		log.info("Invoked :: AuthenticationServiceImpl :: isAgent()");
		User user = this.currentUser();
		return user.getRoles().stream().anyMatch(role -> role.getRole().equalsIgnoreCase(RoleConstants.AGENT.getValue()));
	}

}
