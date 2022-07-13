package com.toqqa.service;

import com.toqqa.bo.*;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.*;
import com.toqqa.dto.UserRequestDto;
import com.toqqa.dto.UsersDto;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderDto;
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

    private final ProductRepository productRepository;

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public AdminService(UserRepository userRepository, Helper helper,
                        RoleRepository roleRepository, AgentRepository agentRepository,
                        SmeRepository smeRepository, OrderInfoRepository orderInfoRepository, OrderItemRepository orderItemRepository,
                        ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.helper = helper;
        this.roleRepository = roleRepository;
        this.agentRepository = agentRepository;
        this.smeRepository = smeRepository;
        this.orderInfoRepository = orderInfoRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
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

    public ListResponseWithCount<UserBo> listUsersByDate(UsersDto usersDto) {

        Page<User> users = this.userRepository.findByCreatedDate(PageRequest.of(usersDto.getPageNumber(), pageSize), usersDto.getStartDate(), usersDto.getEndDate());
        List<UserBo> userBos = new ArrayList<>();
        Role agent = this.roleRepository.findByRole(RoleConstants.AGENT.getValue());
        Role sme = this.roleRepository.findByRole(RoleConstants.SME.getValue());
        users.forEach(user -> {
            UserBo userBo = new UserBo(user);
            if (user.getRoles().contains(agent)) {
                userBo.setAgentBo(this.toAgentBo(user.getId()));
            }
            if (user.getRoles().contains(sme)) {
                userBo.setSmeBo(this.toSmeBo(user.getId()));
            }
            userBos.add(userBo);
        });
        return new ListResponseWithCount<>(userBos, "List All Users", users.getTotalElements(), usersDto.getPageNumber(), users.getTotalPages());
    }

    public ListResponseWithCount<OrderInfoBo> listOrdersByDate(OrderDto orderDto) {

        Page<OrderInfo> orders = this.orderInfoRepository.findByModificationDate(PageRequest.of(orderDto.getPageNumber(), pageSize), orderDto.getStartDate(), orderDto.getEndDate());
        List<OrderInfoBo> orderInfoBos = new ArrayList<>();
        orders.forEach(info -> {
            List<OrderItem> orderItem = info.getOrderItems();
            List<OrderItemBo> orderItemBo = new ArrayList<>();
            orderItem.forEach(item -> {
                ProductBo productBo = new ProductBo(item.getProduct());
                productBo.setBanner(this.helper.prepareAttachmentResource(item.getProduct().getBanner()));
                orderItemBo.add(new OrderItemBo(item, productBo));
            });
            SmeBo smeBo = new SmeBo(info.getSme());
            smeBo.setBusinessLogo(this.helper.prepareResource(info.getSme().getBusinessLogo()));
            smeBo.setIdProof(this.helper.prepareResource(info.getSme().getIdProof()));
            smeBo.setRegDoc(this.helper.prepareResource(info.getSme().getRegDoc()));
            OrderInfoBo orderInfoBo = new OrderInfoBo(info, orderItemBo, smeBo);
            orderInfoBos.add(orderInfoBo);
        });
        return new ListResponseWithCount<>(orderInfoBos, "List All Orders", orders.getTotalElements(), orderDto.getPageNumber(), orders.getTotalPages());
    }
}