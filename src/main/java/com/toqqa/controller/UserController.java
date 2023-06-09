package com.toqqa.controller;

import com.toqqa.bo.UserBo;
import com.toqqa.payload.AgentRegistration;
import com.toqqa.payload.Response;
import com.toqqa.payload.SmeRegistration;
import com.toqqa.payload.UpdateUser;
import com.toqqa.service.AgentService;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.SmeService;
import com.toqqa.service.UserService;
import com.toqqa.service.impls.PushNotificationService;
import com.toqqa.util.Helper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;


    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private SmeService smeService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private Helper helper;


    @ApiOperation(value = "Returns User data by given id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/fetchUser/{id}")
    public Response<UserBo> fetchUser(@PathVariable("id") @Valid String id) {
        log.info("Invoked:: UserController:: fetchUser");
        return new Response<UserBo>(this.userService.fetchUser(id), "success");
    }

    @ApiOperation(value = "Returns logged in uses data")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/currentUser")
    public Response<UserBo> currentUser() {
        log.info("Invoked:: UserController:: currentUser");
        UserBo userBo = new UserBo(this.authenticationService.currentUser());
        userBo.setProfilePicture(this.helper.prepareResource(userBo.getProfilePicture()));
        return new Response<UserBo>(userBo, "success");
    }

    @ApiOperation(value = "Update User")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping("/updateuser")
    public Response<UserBo> updateUser(@ModelAttribute @Valid UpdateUser updateUser) {
        log.info("Invoked:: UserController:: updateUser");
        return new Response<UserBo>(this.userService.updateUser(updateUser), "success");
    }


    @ApiOperation(value = "Become a Sme")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/register_seller")
    public Response registerAsSeller(@ModelAttribute @Valid SmeRegistration smeRegistration) {
        log.info("Invoked:: UserController:: registerAsSeller()");
        return new Response(this.smeService.becomeASme(smeRegistration), "success");
    }

    @ApiOperation(value = "Become an Agent")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/register_agent")
    public Response registerAsAgent(@ModelAttribute @Valid AgentRegistration agentRegistration) {
        log.info("Invoked:: UserController:: registerAsSeller()");
        return new Response(this.agentService.becomeAnAgent(agentRegistration), "success");
    }

}
