package com.toqqa.controller;

import com.toqqa.dto.OrderInfoDto;
import com.toqqa.dto.UserRequestDto;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;
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
    @ApiOperation(value = "check orders placed in a specific time period or by status")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/orders")
    public Response orders(@RequestBody @Valid OrderInfoDto orderInfoDto){
        log.info("Invoked -+- AdminController -+- orders()");
        return this.adminService.orders(orderInfoDto);
    }

}
