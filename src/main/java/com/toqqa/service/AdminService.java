package com.toqqa.service;

import com.toqqa.bo.*;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.*;
import com.toqqa.dto.AdminFilterDto;
import com.toqqa.dto.UserRequestDto;
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
import java.util.stream.Collectors;

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
                        SmeRepository smeRepository, OrderInfoRepository orderInfoRepository) {
        this.userRepository = userRepository;
        this.helper = helper;
        this.roleRepository = roleRepository;
        this.agentRepository = agentRepository;
        this.smeRepository = smeRepository;
        this.orderInfoRepository = orderInfoRepository;
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
                userBo.setAgentBo(this.toAgentBo(this.agentRepository.findByUserId(user.getId())));
            }
            if (user.getRoles().contains(sme)) {
                userBo.setSmeBo(this.toSmeBo(this.smeRepository.findByUserId(user.getId())));
            }
            userBos.add(userBo);
        });
        return userBos;
    }

    private AgentBo toAgentBo(Agent agent) {
        AgentBo agentBo = new AgentBo(agent);
        agentBo.setAgentDocuments(agent.getAgentDocuments() != null ? this.helper.prepareResource(agent.getAgentDocuments()) : AdminConstants.NO_AGENT_DOCUMENTS_FOUND);
        agentBo.setProfilePicture(agent.getAgentProfilePicture() != null ? this.helper.prepareResource(agent.getAgentProfilePicture()) : AdminConstants.NO_PROFILE_PICTURE_FOUND);
        agentBo.setIdProof(agent.getIdProof() != null ? this.helper.prepareResource(agent.getIdProof()) : AdminConstants.NO_ID_PROOF_FOUND);
        return agentBo;
    }

    private SmeBo toSmeBo(Sme sme) {
        SmeBo smeBo = new SmeBo(sme);
        smeBo.setBusinessLogo(sme.getBusinessLogo() != null ? this.helper.prepareResource(sme.getBusinessLogo()) : AdminConstants.NO_BUSINESS_LOGO_FOUND);
        smeBo.setIdProof(sme.getIdProof() != null ? this.helper.prepareResource(sme.getIdProof()) : AdminConstants.NO_ID_PROOF_FOUND);
        smeBo.setRegDoc(sme.getRegDoc() != null ? this.helper.prepareResource(sme.getRegDoc()) : AdminConstants.NO_REGISTRATION_DOCUMENT_FOUND);
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


    public Response recentOrders() {
        log.info("Invoked -+- AdminService -+- recentOrders()");
        List<OrderInfo> orders = this.orderInfoRepository.findFirst4ByOrderByCreatedDateDesc();
        return orders.equals(null) ? new Response(null, AdminConstants.NO_RECENT_ORDERS_FOUND) :
                new Response(orders.stream().map(
                        orderInfo -> new OrderInfoBo(orderInfo,
                                orderInfo.getOrderItems().stream().map(
                                        orderItem -> {
                                            ProductBo productBo = new ProductBo(orderItem.getProduct());
                                            productBo.setImages(this.helper.prepareProductAttachments(orderItem.getProduct().getAttachments()));
                                            return new OrderItemBo(orderItem, productBo);
                                        }
                                ).collect(Collectors.toList()),
                                null)).collect(Collectors.toList()), AdminConstants.RECENT_ORDERS_RETURNED);
    }

    public Response statsByDate(AdminFilterDto adminFilterDto) {
        log.info("Invoked -+- AdminService -+- statsByDate");
        return new Response(new StatsBo(
                this.orderInfoRepository.findTotalAmountByDate(adminFilterDto.getStartDate(), adminFilterDto.getEndDate()).orElse(0.0),
                (long) this.userRepository.findByCreatedDate(adminFilterDto.getStartDate(), adminFilterDto.getEndDate()).size(),
                (long) this.orderInfoRepository.findByCreatedDate(adminFilterDto.getStartDate(), adminFilterDto.getEndDate()).size()),
                AdminConstants.DASHBOARD_STATS);
    }

    public Response newUsers() {
        log.info("Invoked -+- AdminService -+- newUsers()");
        return new Response(this.userRepository.findFirst4ByOrderByCreatedAtDesc().stream().map(
                user -> {
                    UserBo userBo = new UserBo(user);
                    user.setProfilePicture(user.getProfilePicture() != null ? this.helper.prepareResource(user.getProfilePicture()) : AdminConstants.NO_PROFILE_PICTURE_FOUND);
                    return userBo;
                }).collect(Collectors.toList()),
                AdminConstants.NEW_USERS_RETURNED);
    }

    public Response newApprovalRequests() {
        return null;
    }

    public Response approvalRequests() {
        return null;
    }


   /* public ListResponseWithCount<UserBo> listUsersByDate(UsersDto usersDto) {

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
    }*/

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
            SmeBo smeBo = this.toSmeBo(info.getSme());
            OrderInfoBo orderInfoBo = new OrderInfoBo(info, orderItemBo, smeBo);
            orderInfoBos.add(orderInfoBo);
        });
        return new ListResponseWithCount<>(orderInfoBos, AdminConstants.ORDER_LIST, orders.getTotalElements(), orderDto.getPageNumber(), orders.getTotalPages());
    }


    public Response manageUsersByDate(AdminFilterDto adminFilterDto) {
        log.info("Invoked -+- AdminService -+- manageUsersByDate");
        List<User> users = this.userRepository.findByCreatedDate(adminFilterDto.getStartDate(), adminFilterDto.getEndDate());
        List<Sme> smes = this.smeRepository.findAll(false);
        List<Agent> agents = this.agentRepository.findAll();
        return new Response(new TotalUsersBo(
                (long) users.size(),
                (long) smes.size(),
                (long) agents.size()),
                AdminConstants.TOTAL_USERS);
    }
}