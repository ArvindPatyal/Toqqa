package com.toqqa.config;

import com.toqqa.constants.RoleConstants;
import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.User;
import com.toqqa.domain.VerificationStatus;
import com.toqqa.exception.AuthenticationException;
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
        boolean result=false;
            String userType = request.getHeader("UserType");
            if(!helper.notNullAndBlank(userType)){
                throw new BadRequestException("header not found!! Please add UserType header!!");
            }
            if(userType.equals(RoleConstants.CUSTOMER.getValue())){
                return true;
            }
            String endpoint=request.getRequestURI();
            String token = this.getJwtFromRequest(request);
                this.jwtUtil.validateToken(token);
                User user = service.findByEmailOrPhone(jwtUtil.extractUsername(token));

            List<VerificationStatus> statusList = verificationStatusRepository.findByUser(user);
            if(statusList.size()>0) {
                if (isSMEEndpoint(endpoint)) {
                    result = statusList.stream().anyMatch(verificationStatus -> verificationStatus.getStatus().equals(VerificationStatusConstants.ACCEPTED) && verificationStatus.getRole().equals(RoleConstants.SME.getValue()));

                } else {
                    result = statusList.stream().anyMatch(verificationStatus -> verificationStatus.getStatus().equals(VerificationStatusConstants.ACCEPTED) && verificationStatus.getRole().equals(RoleConstants.AGENT.getValue()));
                }
            }

        if(result){
            return result;
        }
        throw new AccessDeniedException("your application request is either pending or declined, please contact admin!!");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (!bearerToken.isEmpty() && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isSMEEndpoint(String endpoint){
        return endpoint.toLowerCase().contains("sme");
    }
    private boolean isAgentEndpoint(String endpoint){
        return endpoint.toLowerCase().contains("agent");
    }
}
