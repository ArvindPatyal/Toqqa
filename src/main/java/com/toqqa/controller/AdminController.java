package com.toqqa.controller;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.UserBo;
import com.toqqa.dto.UserRequestDto;
import com.toqqa.dto.UsersDto;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderDto;
import com.toqqa.payload.Response;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.AdminService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(value = "api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "Returns a page of users")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/users")
    public ListResponseWithCount users(@RequestBody @Valid UserRequestDto userRequestDto) {
        log.info("Invoked -+- AdminController -+- users()");
        return this.adminService.users(userRequestDto);
    }

    @ApiOperation(value = "Enable or Disable a user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping(value = "/user")
    public Response toggleUser(@RequestParam String id) {
        log.info("Invoked -+- AdminController -+-toggleUser()");
        return this.adminService.toggleUser(id);
    }
    @ApiOperation(value = "check user register in a specific time period")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/findUsersByDate")
    public ListResponseWithCount<UserBo> listUsersByDate (@RequestBody @Valid UsersDto usersDto) {
        log.info("Invoked -+- AdminController -+- listUsersByDate()");
        return this.adminService.listUsersByDate(usersDto);
    }
    @ApiOperation(value = "check order status in a specific time period")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/findOrdersByDate")
    public ListResponseWithCount<OrderInfoBo> listOrdersByDate(@RequestBody @Valid OrderDto orderDto) {
        log.info("Invoked -+- AdminController -+- listOrdersByDate()");
        return this.adminService.listOrdersByDate(orderDto);
    }
}
