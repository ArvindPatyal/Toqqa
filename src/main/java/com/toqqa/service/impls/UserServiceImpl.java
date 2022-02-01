package com.toqqa.service.impls;

import com.toqqa.bo.UserBo;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.User;
import com.toqqa.exception.UserAlreadyExists;
import com.toqqa.payload.UserSignUp;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.UserService;
import com.toqqa.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Helper helper;

    @Override
    public UserBo addUser(UserSignUp userSignUp) {
        if(!isUserExists(userSignUp.getEmail(),userSignUp.getPhone())){
            User user = new User();
            user.setCity(userSignUp.getCity());
            user.setCountry(userSignUp.getCountry());
            user.setAgentId(userSignUp.getAgentId());
            user.setDeleted(false);
            user.setEmail(userSignUp.getEmail());
            user.setFirstName(userSignUp.getFirstName());
            user.setPhone(userSignUp.getPhone());
            user.setLastName(userSignUp.getLastName());
            user.setPostCode(userSignUp.getPostCode());
            user.setState(userSignUp.getState());
            user.setAddress(userSignUp.getAddress());
            user.setPassword(new BCryptPasswordEncoder().encode(userSignUp.getPassword()));

            user.setRoles(Arrays.asList(this.roleRepository.findByRole(RoleConstants.CUSTOMER.getValue())));

            user=this.userRepository.saveAndFlush(user);
            return new UserBo(user);
        }
        throw new UserAlreadyExists("user already exists");
    }

    @Override
    public Boolean isUserExists(String email, String phone) {
            User user =this.userRepository.findByEmailOrPhone(email,phone);
            if(user!=null){
                return true;
            }
            return false;
    }
}
