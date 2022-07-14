package com.toqqa.service.impls;

import com.toqqa.bo.UserBo;
import com.toqqa.config.JWTConfig;
import com.toqqa.constants.FolderConstants;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.Device;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ResourceCreateUpdateException;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.exception.UserAlreadyExists;
import com.toqqa.payload.*;
import com.toqqa.repository.DeviceRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.DeliveryAddressService;
import com.toqqa.service.StorageService;
import com.toqqa.service.UserService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    private StorageService storageService;

    @Autowired
    @Lazy
    private AuthenticationManager manager;

    @Autowired
    @Lazy
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    @Lazy
    private AuthenticationService authenticationService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserBo addUser(UserSignUp userSignUp) {
        log.info("Invoked :: UserServiceImpl :: addUser()");
        if (isUserExists(userSignUp.getEmail(), userSignUp.getPhone())) {
            throw new UserAlreadyExists("user with email/number already exists");
        }
        User user = new User();
        user.setCity(userSignUp.getCity());
        user.setCountry(userSignUp.getCountry());
        user.setAgentId(userSignUp.getAgentId());
        user.setIsDeleted(false);
        user.setCreatedAt(new Date());
        if (this.helper.notNullAndBlank(userSignUp.getEmail())) {
            if (!EmailValidator.getInstance().isValid(userSignUp.getEmail())) {
                throw new BadRequestException("invalid email value : " + userSignUp.getEmail());
            }
            user.setEmail(userSignUp.getEmail().toLowerCase());
        }
        user.setFirstName(userSignUp.getFirstName());
        if (this.helper.notNullAndBlank(userSignUp.getPhone())) {
            if (!this.helper.isValidNumber(userSignUp.getPhone())) {
                throw new BadRequestException("invalid phone number :" + userSignUp.getPhone());
            }
            user.setPhone(userSignUp.getPhone());
        }
        user.setLastName(userSignUp.getLastName());
        user.setPostCode(userSignUp.getPostCode());
        user.setState(userSignUp.getState());
        user.setAddress(userSignUp.getAddress());
        user.setLatitude(userSignUp.getLatitude());
        user.setLongitude(userSignUp.getLongitude());
        user.setPassword(new BCryptPasswordEncoder().encode(userSignUp.getPassword()));
        user.setRoles(Arrays.asList(this.roleRepository.findByRole(RoleConstants.CUSTOMER.getValue())));
        user = this.userRepository.saveAndFlush(user);
        this.deliveryAddressService.create(user);
        return new UserBo(user);

    }

    @Override
    public Boolean isUserExists(String email, String phone) {
        log.info("Invoked :: UserServiceImpl :: isUserExists()");
        User user = null;
        if (this.helper.notNullAndBlank(email) || this.helper.notNullAndBlank(phone)) {
            if (this.helper.notNullAndBlank(email)) {
                user = this.userRepository.findByEmail(email);
            }
            if (user == null) {
                user = this.userRepository.findByPhone(phone);
            }
            return user != null;

        }
        throw new BadRequestException("No email and phone found");
    }

    @Override
    public User findByEmailOrPhone(String username) {
        log.info("Invoked :: UserServiceImpl :: findByEmailOrPhone()");
        return this.userRepository.findByEmailOrPhone(username, username);
    }// find user by emailid or phone

    @Override
    public LoginResponse signIn(LoginRequest request) {
        log.info("Invoked :: UserServiceImpl :: signIn()");
        try {
            Authentication authentication = this.manager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse(
                    this.jwtConfig.generateToken(request.getUsername()));
            authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = this.userRepository.findByEmailOrPhone(authentication.getName(), authentication.getName());
            UserBo userBoObj = new UserBo(user);
            userBoObj.setProfilePicture(this.helper.prepareResource(user.getProfilePicture()));

            this.token(user, request);

            return new LoginResponse(jwtAuthenticationResponse, userBoObj);
        } catch (Exception e) {
            log.error("Exception in :: UserServiceImpl :: signIn() ::" + e.getLocalizedMessage());
            throw new BadCredentialsException("invalid login credentials");
        }

    }

    private void token(User user, LoginRequest loginRequest) {
        Optional<Device> optionalDevice = this.deviceService.getByToken(loginRequest.getDeviceToken());
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            device.setUser(user);
            this.deviceRepository.saveAndFlush(device);
        } else {
            deviceService.addDevice(user, loginRequest.getDeviceToken());
        }

    }

    @Override
    public UserBo fetchUser(String id) {
        log.info("Invoked :: UserServiceImpl :: fetchUser()");
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return new UserBo(user.get());
        }
        throw new BadRequestException("no user found with id= " + id);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        log.info("Invoked :: UserServiceImpl :: loadUserByUsername()");
        User user = userRepository.findByEmailOrPhone(userName, userName);
        if (user != null && !user.getIsDeleted()) {
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(userName, user.getPassword(), authorities);

        } else {
            throw new UsernameNotFoundException("user not found");
        }

    }// verify user by email/username

    @Override
    public UserBo updateUser(UpdateUser updateUser) {
        log.info("Invoked :: UserServiceImpl :: updateUser()");
        User user = this.authenticationService.currentUser();
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        if(helper.notNullAndBlank(updateUser.getEmail())){
            if (!EmailValidator.getInstance().isValid(updateUser.getEmail())) {
                throw new BadRequestException("invalid email value : " + updateUser.getEmail());
            }else{
                 if(this.userRepository.findByEmail(updateUser.getEmail())==null){
                     user.setEmail(updateUser.getEmail());
                 }else {
                     throw new BadRequestException("Email is already in use");
                 }

            }
        }

        if (updateUser.getProfilePicture() != null && !updateUser.getProfilePicture().isEmpty()) {
            try {
                user.setProfilePicture(this.storageService.uploadFileAsync(updateUser.getProfilePicture(),
                        user.getId(),
                        FolderConstants.PROFILE_PICTURE.getValue()).get());
            } catch (InterruptedException | ExecutionException e) {
                throw new ResourceCreateUpdateException("Cannot update Profile Picture");
            }
        }
        user = this.userRepository.saveAndFlush(user);
        UserBo bo = new UserBo(user);
        bo.setProfilePicture(this.helper.prepareResource(user.getProfilePicture()));
        return bo;

    }


    @Override
    public User getById(String id) {
        Optional<User> userObj = userRepository.findById(id);
        if (userObj.isPresent()) {
            return userObj.get();
        } else {
            throw new ResourceNotFoundException("Resource doesnt exist");

        }
    }
}
