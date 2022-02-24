package com.toqqa.service.impls;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.toqqa.payload.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.toqqa.bo.UserBo;
import com.toqqa.config.JWTConfig;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.UserAlreadyExists;
import com.toqqa.payload.JwtAuthenticationResponse;
import com.toqqa.payload.LoginRequest;
import com.toqqa.payload.UserSignUp;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.UserService;
import com.toqqa.util.Helper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private Helper helper;
	@Autowired
	private JWTConfig jwtConfig;

	@Autowired
	@Lazy
	private AuthenticationManager manager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserBo addUser(UserSignUp userSignUp) {
		log.info("Inside add user");
		if (isUserExists(userSignUp.getEmail(), userSignUp.getPhone())) {
			throw new UserAlreadyExists("user already exists");
		}
		User user = new User();
		user.setCity(userSignUp.getCity());
		user.setCountry(userSignUp.getCountry());
		user.setAgentId(userSignUp.getAgentId());
		user.setIsDeleted(false);
		user.setEmail(this.helper.notNullAndBlank(userSignUp.getEmail()) ? userSignUp.getEmail() : null);
		user.setFirstName(userSignUp.getFirstName());
		user.setPhone(this.helper.notNullAndBlank(userSignUp.getPhone()) ? userSignUp.getPhone() : null);
		user.setLastName(userSignUp.getLastName());
		user.setPostCode(userSignUp.getPostCode());
		user.setState(userSignUp.getState());
		user.setAddress(userSignUp.getAddress());
		user.setPassword(new BCryptPasswordEncoder().encode(userSignUp.getPassword()));
		user.setRoles(Arrays.asList(this.roleRepository.findByRole(RoleConstants.CUSTOMER.getValue())));
		user = this.userRepository.saveAndFlush(user);
		return new UserBo(user);

	}

	@Override
	public Boolean isUserExists(String email, String phone) {
		log.info("Inside is user exists");
		User user = null;
		if (this.helper.notNullAndBlank(email) || this.helper.notNullAndBlank(phone)) {
			if (this.helper.notNullAndBlank(email)) {
				user = this.userRepository.findByEmail(email);
			}
			if (this.helper.notNullAndBlank(phone)) {
				user = this.userRepository.findByPhone(phone);
			}
			return user != null;
		}
		throw new BadRequestException("email or phone is required");
	}

	@Override
	public User findByEmailOrPhone(String username) {
		log.info("Inside find by email or phone");
		return this.userRepository.findByEmailOrPhone(username, username);
	}// find user by emailid or phone

	@Override
	public LoginResponse signIn(LoginRequest bo) {
		log.info("Inside SignIn");
		try {
			Authentication authentication = this.manager
					.authenticate(new UsernamePasswordAuthenticationToken(bo.getUsername(), bo.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse(
					this.jwtConfig.generateToken(bo.getUsername()));
			authentication = SecurityContextHolder.getContext().getAuthentication();
			UserBo user = new UserBo(
					this.userRepository.findByEmailOrPhone(authentication.getName(), authentication.getName()));
			return new LoginResponse(jwtAuthenticationResponse, user);
		} catch (Exception e) {
			throw new BadCredentialsException("bad Credentials");
		}

	}// send auth token

	@Override
	public UserBo fetchUser(String id) {
		log.info("Inside fetch User");
		Optional<User> user = this.userRepository.findById(id);
		if (user.isPresent()) {
			return new UserBo(user.get());
		}
		throw new BadRequestException("no user found with id= " + id);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) {
		log.info("Inside load user by username");
		User user = userRepository.findByEmailOrPhone(userName, userName);
		if (user != null && !user.getIsDeleted()) {
			List<GrantedAuthority> authorities = user.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
			return new org.springframework.security.core.userdetails.User(userName, user.getPassword(), authorities);

		} else {
			throw new UsernameNotFoundException("user not found");
		}

	}// verify user by email/username
}
