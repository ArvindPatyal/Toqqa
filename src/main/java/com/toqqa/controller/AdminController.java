package com.toqqa.controller;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.UserBo;
import com.toqqa.dto.AdminFilterDto;
import com.toqqa.dto.UserRequestDto;
import com.toqqa.payload.ApprovalPayload;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderDto;
import com.toqqa.payload.Response;
import com.toqqa.service.AdminService;
import com.toqqa.service.UserService;
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
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }


    @ApiOperation(value = "extract user from token")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequestMapping(method = RequestMethod.GET, value = "/extractUser")
    public Response userFromToken() {
        return this.adminService.userFromToken();
    }

   /* @ApiOperation(value = "Returns a page of users")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/users")
    public ListResponseWithCount users(@RequestBody @Valid UserRequestDto userRequestDto) {
        log.info("Invoked -+- AdminController -+- users()");
        return this.adminService.users(userRequestDto);
    }*/

    /*@ApiOperation(value = "Enable or Disable a user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping(value = "/user")
    public Response toggleUser(@RequestParam String id) {
        log.info("Invoked -+- AdminController -+-toggleUser()");
        return this.adminService.toggleUser(id);
    }*/

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

    @ApiOperation(value = "Approval requests")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/approval_requests")
    public Response allApprovalRequests() {
        log.info("Invoked -+- AdminController -+- allApprovalRequests");
        return this.adminService.allApprovalRequests();
    }

//    @ApiOperation(value = "check user register in a specific time period")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
//            @ApiResponse(code = 400, message = "Bad Request")})
//    @PostMapping(value = "/findUsersByDate")
//    public ListResponseWithCount<UserBo> listUsersByDate(@RequestBody @Valid UsersDto usersDto) {
//        log.info("Invoked -+- AdminController -+- listUsersByDate()");
//        return this.adminService.listUsersByDate(usersDto);
//    }

 /*   @ApiOperation(value = "check order status in a specific time period")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/findOrdersByDate")
    public ListResponseWithCount<OrderInfoBo> listOrdersByDate(@RequestBody @Valid OrderDto orderDto) {
        log.info("Invoked -+- AdminController -+- listOrdersByDate()");
        return this.adminService.listOrdersByDate(orderDto);
    }*/

    @ApiOperation(value = "check new users, orders and sale status in a specific time period")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping(value = "/manageUsers")
    public Response manageUsersByDate(@RequestBody @Valid AdminFilterDto adminFilterDto) {
        log.info("Invoked -+- AdminController -+- statsByDate()");
        return this.adminService.userStatsByDate(adminFilterDto);
    }

    @ApiOperation(value = "Top 4 new users")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/new_users")
    public Response newUsers() {
        log.info("Invoked -+- AdminController -+- newUsers()");
        return this.adminService.newUsers();
    }

    @ApiOperation(value = "all users")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/all_users")
    public Response allUsers() {
        log.info("Invoked -+- AdminController -+- allUsers()");
        return this.adminService.allUsers();
    }

    @ApiOperation(value = "Recent 4 approval requests")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping(value = "/new_approval_requests")
    public Response newApprovalRequests() {
        log.info("Invoked -+- AdminController -+- newApprovalRequests");
        return this.adminService.newApprovalRequests();
    }

    @ApiOperation(value = "Approve verification request")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping(value = "/approve")
    public Response approve(@RequestBody @Valid ApprovalPayload approvalPayload) {
        log.info("Invoked -+- AdminController -+- approvalRequests");
        return this.adminService.approve(approvalPayload);
    }

   /* @ApiOperation(value = "User details")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/fetchUser/{id}")
    public Response<UserBo> fetchUser(@PathVariable("id") @Valid String id) {
        log.info("Invoked:: UserController:: fetchUser");
        return new Response<UserBo>(this.userService.fetchUser(id), "success");
    }*/
}
