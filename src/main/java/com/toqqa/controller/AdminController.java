package com.toqqa.controller;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.dto.AdminFilterDto;
import com.toqqa.dto.UserRequestDto;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderDto;
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

    private final AdminService adminService;

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

    @ApiOperation(value = "Top 4 new users")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/new_users")
    public Response newUsers() {
        log.info("Invoked -+- AdminController -+- newUsers()");
        return this.adminService.newUsers();
    }

    @ApiOperation(value = "Enable or Disable a user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping(value = "/user")
    public Response toggleUser(@RequestParam String id) {
        log.info("Invoked -+- AdminController -+-toggleUser()");
        return this.adminService.toggleUser(id);
    }

    @ApiOperation(value = "Recent orders on Dashboard")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/recent_orders")
    public Response recentOrders() {
        log.info("Invoked -+- AdminController -+- recentOrders()");
        return this.adminService.recentOrders();
    }

    @ApiOperation(value = "check new users, orders and sale status in a specific time period")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/stats")
    public Response statsByDate(@RequestBody @Valid AdminFilterDto adminFilterDto) {
        log.info("Invoked -+- AdminController -+- statsByDate()");
        return this.adminService.statsByDate(adminFilterDto);
    }

    @ApiOperation(value = "Recent 4 approval requests")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/new_approval_requests")
    public Response newApprovalRequests() {
        log.info("Invoked -+- AdminController -+- newApprovalRequests");
        return this.adminService.newApprovalRequests();
    }

    @ApiOperation(value = "Approval requests")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/approval_requests")
    public Response approvalRequests() {
        log.info("Invoked -+- AdminController -+- approvalRequests");
        return this.adminService.approvalRequests();
    }

//     @ApiOperation(value = "Approve verification request")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
//            @ApiResponse(code = 400, message = "Bad Request")})
//    @PutMapping(value = "/approve")
//    public Response approve() {
//        log.info("Invoked -+- AdminController -+- approvalRequests");
//        return this.adminService.approve();
//    }


//    @ApiOperation(value = "check user register in a specific time period")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
//            @ApiResponse(code = 400, message = "Bad Request")})
//    @PostMapping(value = "/findUsersByDate")
//    public ListResponseWithCount<UserBo> listUsersByDate(@RequestBody @Valid UsersDto usersDto) {
//        log.info("Invoked -+- AdminController -+- listUsersByDate()");
//        return this.adminService.listUsersByDate(usersDto);
//    }

    @ApiOperation(value = "check order status in a specific time period")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/findOrdersByDate")
    public ListResponseWithCount<OrderInfoBo> listOrdersByDate(@RequestBody @Valid OrderDto orderDto) {
        log.info("Invoked -+- AdminController -+- listOrdersByDate()");
        return this.adminService.listOrdersByDate(orderDto);
    }


}
