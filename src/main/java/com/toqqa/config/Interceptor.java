package com.toqqa.config;

import com.toqqa.constants.RoleConstants;
import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.User;
import com.toqqa.domain.VerificationStatus;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ExceptionHandlingConfig;
import com.toqqa.repository.VerificationStatusRepository;
import com.toqqa.service.UserService;
import com.toqqa.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
public class Interceptor implements HandlerInterceptor {

    @Autowired
    private VerificationStatusRepository verificationStatusRepository;

    @Autowired
    private JWTConfig jwtUtil;

    @Autowired
    private UserService service;

    @Autowired
    private ExceptionHandlingConfig exceptionHandlingConfig;

    @Autowired
    private Helper helper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = false;
        String userType = request.getHeader("UserType");
        if (!helper.notNullAndBlank(userType)) {
            throw new BadRequestException("header not found!! Please add UserType header!!");
        }
        if (userType.equals(RoleConstants.CUSTOMER.getValue())) {
            return true;
        }
        String endpoint = request.getRequestURI();
        String token = this.getJwtFromRequest(request);
        this.jwtUtil.validateToken(token);
        User user = service.findByEmailOrPhone(jwtUtil.extractUsername(token));

        List<VerificationStatus> statusList = verificationStatusRepository.findByUser(user);
        if (statusList.size() > 0) {
            if (isSMEEndpoint(endpoint)) {
                return statusList.stream().anyMatch(verificationStatus -> verificationStatus.getStatus().equals(VerificationStatusConstants.ACCEPTED) && verificationStatus.getRole().equals(RoleConstants.SME));
            } else if (isAgentEndpoint(endpoint)) {
                return statusList.stream().anyMatch(verificationStatus -> verificationStatus.getStatus().equals(VerificationStatusConstants.ACCEPTED) && verificationStatus.getRole().equals(RoleConstants.AGENT));
            } else {
                throw new AccessDeniedException("your application request is either pending or declined, please contact admin!!");
            }
        }
        throw new AccessDeniedException("You have not applied for sme or agent for now!!!!");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (!bearerToken.isEmpty() && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

//    private String endPoints(String endPoint){
//        if (endPoint.toLowerCase().contains("api/sme"))
//            return "sme";
//        else if (endPoint.toLowerCase().contains("api/agent"))
//            return "agent";
//        else if (endPoint.toLowerCase().contains("api/")) {
//
//        }
//    }

    private boolean isSMEEndpoint(String endpoint) {
        if (endpoint.toLowerCase().contains("sme")) {
            return true;
        } else if (endpoint.toLowerCase().contains("product")) {
            return true;
        } else if (endpoint.toLowerCase().contains("advertisement")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isAgentEndpoint(String endpoint) {
        return endpoint.toLowerCase().contains("agent");
    }

}
