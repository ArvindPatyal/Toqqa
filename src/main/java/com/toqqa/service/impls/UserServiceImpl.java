package com.toqqa.service.impls;

import com.toqqa.bo.UserBO;
import com.toqqa.payload.UserSignUp;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.UserService;
import com.toqqa.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Helper helper;

    @Override
    public UserBO addUser(UserSignUp userSignUp) {
        return null;
    }

    @Override
    public Boolean isUserExists(String email, String phone) {
        if(this.helper.notNullAndBlank(email)&& this.helper.notNullAndBlank(phone)){

        }
        return null;
    }

}
