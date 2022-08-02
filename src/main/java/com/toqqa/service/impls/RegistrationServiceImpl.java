package com.toqqa.service.impls;

import com.toqqa.bo.AgentBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.bo.UserBo;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.User;
import com.toqqa.dto.AdminRegistrationDto;
import com.toqqa.exception.UserAlreadyExists;
import com.toqqa.payload.*;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.AgentService;
import com.toqqa.service.RegistrationService;
import com.toqqa.service.SmeService;
import com.toqqa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private SmeService smeService;
    @Autowired
    @Lazy
    private RoleRepository roleRepository;
    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response<?> registerAgent(AgentRegistrationPayload payload) {
        log.info("Invoked :: RegistrationServiceImpl :: registerAgent()");
        if (userService.isUserExists(payload.getUserSignUp().getEmail(), payload.getUserSignUp().getPhone())) {
            throw new UserAlreadyExists("user with email/number already exists");
        }
        UserBo user = this.userService.addUser(payload.getUserSignUp());
        AgentBo agentBo = this.agentService.agentRegistration(payload.getAgentRegistration(), user.getId(), true);
        AgentRegistrationResponse registrationResponse = new AgentRegistrationResponse();
        registrationResponse.setUser(this.userService.fetchUser(user.getId()));
        registrationResponse.setAgent(agentBo);
        return new Response<AgentRegistrationResponse>(registrationResponse, "success");
    }

    @Override
    public Response<?> registerSme(SmeRegistrationPayload payload) {
        log.info("Invoked :: RegistrationServiceImpl :: registerSme()");
        if (userService.isUserExists(payload.getUserSignUp().getEmail(), payload.getUserSignUp().getPhone())) {
            throw new UserAlreadyExists("user with email/number already exists");
        }
        UserBo user = this.userService.addUser(payload.getUserSignUp());
        SmeBo sme = this.smeService.smeRegistration(payload.getSmeRegistration(), user.getId(), true);
        SmeRegistrationResponse registrationResponse = new SmeRegistrationResponse();
        registrationResponse.setUser(this.userService.fetchUser(user.getId()));
        registrationResponse.setSme(sme);
        return new Response<SmeRegistrationResponse>(registrationResponse, "success");
    }

    @Override
    public ResponseEntity adminRegistration(AdminRegistrationDto adminRegistrationDto) {
        if (this.userService.isUserExists(adminRegistrationDto.getEmail(), adminRegistrationDto.getPhone())) {
            throw new UserAlreadyExists("NUMBER OR EMAIL ALREADY IN USE");
        }
        User user = new User();
        user.setFirstName(adminRegistrationDto.getFirstName());
        user.setLastName(adminRegistrationDto.getLastName());
        user.setPassword(new BCryptPasswordEncoder().encode(adminRegistrationDto.getPassword()));
        user.setCountry(adminRegistrationDto.getCountry());
        user.setPhone(adminRegistrationDto.getPhone());
        user.setRoles(Collections.singletonList(this.roleRepository.findByRole(RoleConstants.ADMIN.getValue())));
        user.setCity(adminRegistrationDto.getCity());
        user.setState(adminRegistrationDto.getState());
        user = this.userRepository.saveAndFlush(user);
        return new ResponseEntity(new UserBo(user), HttpStatus.OK);
    }


}
