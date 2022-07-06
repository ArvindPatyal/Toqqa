package com.toqqa.service;

import com.toqqa.bo.AgentBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.bo.UserBo;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.*;
import com.toqqa.dto.OrderInfoDto;
import com.toqqa.dto.UserRequestDto;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;
import com.toqqa.repository.*;
import com.toqqa.util.AdminConstants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminService {
    @Value("${pageSize}")
    private Integer pageSize;
    private final UserRepository userRepository;
    private final Helper helper;
    private final RoleRepository roleRepository;
    private final AgentRepository agentRepository;
    private final SmeRepository smeRepository;
    private final OrderInfoRepository orderInfoRepository;

    @Autowired
    public AdminService(UserRepository userRepository, Helper helper,
                        RoleRepository roleRepository, AgentRepository agentRepository,
                        SmeRepository smeRepository,OrderInfoRepository orderInfoRepository) {
        this.userRepository = userRepository;
        this.helper = helper;
        this.roleRepository = roleRepository;
        this.agentRepository = agentRepository;
        this.smeRepository = smeRepository;
        this.orderInfoRepository=orderInfoRepository;
    }

    public ListResponseWithCount users(UserRequestDto userRequestDto) {
        log.info("Invoked -+- AdminService -+- users()");
        Page<User> users = this.userRepository.findAll(PageRequest.of(userRequestDto.getPageNumber(), pageSize, Sort.by(userRequestDto.getSortOrder(), AdminConstants.USER_LIST_SORT_BY)));
        List<UserBo> userBos = this.userToUserBo(users);
        return new ListResponseWithCount(userBos, AdminConstants.LIST_OF_USERS_RETURNED, users.getTotalElements(), userRequestDto.getPageNumber(), users.getTotalPages());
    }

    private List<UserBo> userToUserBo(Page<User> users) {
        List<UserBo> userBos = new ArrayList<>();
        Role agent = this.roleRepository.findByRole(RoleConstants.AGENT.getValue());
        Role sme = this.roleRepository.findByRole(RoleConstants.SME.getValue());
        users.forEach(user -> {
            UserBo userBo = new UserBo(user);
            userBo.setProfilePicture(user.getProfilePicture() != null ? this.helper.prepareResource(user.getProfilePicture()) : AdminConstants.NO_PROFILE_PICTURE_FOUND);
            if (user.getRoles().contains(agent)) {
                userBo.setAgentBo(this.toAgentBo(user.getId()));
            }
            if (user.getRoles().contains(sme)) {
                userBo.setSmeBo(this.toSmeBo(user.getId()));
            }
            userBos.add(userBo);
        });
        return userBos;
    }

    private AgentBo toAgentBo(String userId) {
        Agent agent = this.agentRepository.findByUserId(userId);
        AgentBo agentBo = null;
        if (agent != null) {
            agentBo = new AgentBo(agent);
            agentBo.setAgentDocuments(agent.getAgentDocuments() != null ? this.helper.prepareResource(agent.getAgentDocuments()) : AdminConstants.NO_AGENT_DOCUMENTS_FOUND);
            agentBo.setProfilePicture(agent.getAgentProfilePicture() != null ? this.helper.prepareResource(agent.getAgentProfilePicture()) : AdminConstants.NO_PROFILE_PICTURE_FOUND);
            agentBo.setIdProof(agent.getIdProof() != null ? this.helper.prepareResource(agent.getIdProof()) : AdminConstants.NO_ID_PROOF_FOUND);
        }
        return agentBo;
    }

    private SmeBo toSmeBo(String userId) {
        Sme sme = this.smeRepository.findByUserId(userId);
        SmeBo smeBo = null;
        if (sme != null) {
            smeBo = new SmeBo(sme);
            smeBo.setBusinessLogo(sme.getBusinessLogo() != null ? this.helper.prepareResource(sme.getBusinessLogo()) : AdminConstants.NO_BUSINESS_LOGO_FOUND);
            smeBo.setIdProof(sme.getIdProof() != null ? this.helper.prepareResource(sme.getIdProof()) : AdminConstants.NO_ID_PROOF_FOUND);
            smeBo.setRegDoc(sme.getRegDoc() != null ? this.helper.prepareResource(sme.getRegDoc()) : AdminConstants.NO_REGISTRATION_DOCUMENT_FOUND);
        }
        return smeBo;
    }

    public Response toggleUser(String userId) {
        log.info("Invoked -+- AdminService -+- toggleUser()");
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsDeleted(!user.getIsDeleted());
            this.userRepository.saveAndFlush(user);
            return new Response(true, AdminConstants.TOGGLE_USER_STATUS_CHANGED);
        }
        throw new ResourceNotFoundException(AdminConstants.NO_USER_FOUND_WITH_ID + userId);
    }


    public Response orders(OrderInfoDto orderInfoDto) {
        log.info("Invoked -+- AdminService -+- orders()");
//        List<OrderInfo>  orderInfos = this.orderInfoRepository.
        return null;
    }
}
