package com.toqqa.service;

import com.toqqa.bo.*;
import com.toqqa.constants.OrderBy;
import com.toqqa.constants.RoleConstants;
import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.*;
import com.toqqa.dto.AdminFilterDto;
import com.toqqa.dto.AdminPaginationDto;
import com.toqqa.dto.UserDetailsDto;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.payload.ApprovalPayload;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;
import com.toqqa.repository.*;
import com.toqqa.service.impls.PushNotificationService;
import com.toqqa.util.AdminConstants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class AdminService {
    private final UserRepository userRepository;
    private final Helper helper;
    private final RoleRepository roleRepository;
    private final AgentRepository agentRepository;
    private final SmeRepository smeRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final VerificationStatusRepository verificationStatusRepository;
    private final AuthenticationService authenticationService;
    private final PushNotificationService pushNotificationService;
    @Value("${pageSize}")
    private Integer pageSize;


    @Autowired
    public AdminService(UserRepository userRepository, Helper helper,
                        RoleRepository roleRepository, AgentRepository agentRepository,
                        SmeRepository smeRepository, OrderInfoRepository orderInfoRepository,
                        VerificationStatusRepository verificationStatusRepository, AuthenticationService authenticationService,
                        PushNotificationService pushNotificationService) {
        this.userRepository = userRepository;
        this.helper = helper;
        this.roleRepository = roleRepository;
        this.agentRepository = agentRepository;
        this.smeRepository = smeRepository;
        this.orderInfoRepository = orderInfoRepository;
        this.verificationStatusRepository = verificationStatusRepository;
        this.authenticationService = authenticationService;
        this.pushNotificationService = pushNotificationService;
    }

    public Response userFromToken() {
        log.info("Invoked -+- UserServiceImpl -+- userFromToken()");
        UserBo userBo = new UserBo(this.authenticationService.currentUser());
        userBo.setProfilePicture(this.helper.prepareResource(userBo.getProfilePicture()));
        return new Response(userBo, "User details");
    }

//    public ListResponseWithCount users(UserRequestDto userRequestDto) {
//        log.info("Invoked -+- AdminService -+- users()");
//        Page<User> users = this.userRepository.findAll(PageRequest.of(userRequestDto.getPageNumber(), pageSize, Sort.by(userRequestDto.getSortOrder(), AdminConstants.USER_LIST_SORT_BY)));
//        List<UserBo> userBos = this.userToUserBo(users);
//        return new ListResponseWithCount(userBos, AdminConstants.LIST_OF_USERS_RETURNED, users.getTotalElements(), userRequestDto.getPageNumber(), users.getTotalPages());
//    }

   /* private List<UserBo> userToUserBo(Page<User> users) {
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
    }*/

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

//    public Response toggleUser(String userId) {
//        log.info("Invoked -+- AdminService -+- toggleUser()");
//        Optional<User> optionalUser = this.userRepository.findById(userId);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            user.setIsDeleted(!user.getIsDeleted());
//            this.userRepository.saveAndFlush(user);
//            return new Response(true, AdminConstants.TOGGLE_USER_STATUS_CHANGED);
//        }
//        throw new ResourceNotFoundException(AdminConstants.NO_USER_FOUND_WITH_ID + userId);
//    }


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
                                null)), AdminConstants.RECENT_ORDERS_RETURNED);
    }

  /*  public Response allOrders() {
        log.info("Invoked -+- AdminService -+- allOrders()");
        List<OrderInfo> orders = this.orderInfoRepository.findAllByOrderByCreatedDateDesc();
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
    }*/

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
        List<User> users = this.userRepository.findFirst4ByOrderByCreatedAtDesc();
        return new Response(this.usersWithVerificationStatus(users, AdminConstants.VerificationStatus), AdminConstants.NEW_USERS_RETURNED);
    }

    public Response allUsers(UserDetailsDto userDetailsDto) {
        log.info("Invoked -+- AdminService -+- newUsers()");
        if (userDetailsDto.getStatus() == null) {
            userDetailsDto.setStatus(AdminConstants.VerificationStatus);
        }
        Page<User> users = this.userRepository.findAll(PageRequest.of(userDetailsDto.getPageNumber(), pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
        return new Response<AdminPaginationDto>(new AdminPaginationDto<List<UserBo>>(
                this.usersWithVerificationStatus(users.getContent(), userDetailsDto.getStatus()).collect(Collectors.toList()),
                users.getTotalElements(), userDetailsDto.getPageNumber(), users.getTotalPages()),
                "");
    }

    private Stream<UserBo> usersWithVerificationStatus(List<User> users, List<VerificationStatusConstants> verificationStatusConstants) {
        List<VerificationStatus> verificationStatuses = this.verificationStatusRepository.findByUserInAndStatusIn(users, verificationStatusConstants);
        // Role admin = this.roleRepository.findByRole(RoleConstants.ADMIN.getValue());
        // users.removeIf(user -> user.getRoles().contains(admin));
        return users.stream().map(user -> {
            UserBo userBo = new UserBo(user);
            List<VerificationStatus> verificationStatusList = verificationStatuses.stream().filter(verificationStatus -> verificationStatus.getUser().equals(user)).collect(Collectors.toList());
            Map<String, String> verificationMap = new HashMap<>();
            Map<String, String> verificationIdsMap = new HashMap<>();
            verificationStatusList.forEach(verificationStatus -> {
                verificationMap.put(verificationStatus.getRole().getValue(), verificationStatus.getStatus().toString());
                if (verificationStatus.getRole().equals("CUSTOMER")) {
                    verificationStatus.setId("HIDDEN");
                }
                verificationIdsMap.put(verificationStatus.getRole().getValue(), verificationStatus.getId());
            });
            userBo.setVerification(verificationMap);
            userBo.setVerificationIds(verificationIdsMap);
            userBo.setProfilePicture(this.helper.prepareResource(userBo.getProfilePicture()));
            if (userBo.getRoles().contains("ROLE_SME")) {
                Sme sme = this.smeRepository.findByUserId(user.getId());
                userBo.setSmeBo(sme != null ? this.toSmeBo(sme) : null);
            }
            if (userBo.getRoles().contains("ROLE_AGENT")) {
                Agent agent = this.agentRepository.findByUserId(user.getAgentId());
                userBo.setAgentBo(agent != null ? this.toAgentBo(agent) : null);
            }
            return userBo;
        });
    }


    public Response newApprovalRequests() {
        log.info("Invoked -+- AdminService -+- newApprovalRequests");
        List<VerificationStatus> verificationStatuses = this.verificationStatusRepository.findFirst4NewRequest();
        return new Response(this.verificationStatusToBo(verificationStatuses), AdminConstants.APPROVAL_REQUESTS);
    }

    public Response userStatsByDate(AdminFilterDto adminFilterDto) {
        log.info("Invoked -+- AdminService -+- userStatsByDate()");
        List<VerificationStatus> customers = this.verificationStatusRepository.findByCustomerRolesAndStatus(adminFilterDto.getStartDate(), adminFilterDto.getEndDate());
        List<VerificationStatus> smes = this.verificationStatusRepository.findBySmeRolesAndStatus(adminFilterDto.getStartDate(), adminFilterDto.getEndDate());
        List<VerificationStatus> agents = this.verificationStatusRepository.findByAgentRolesAndStatus(adminFilterDto.getStartDate(), adminFilterDto.getEndDate());
        return new Response(new TotalUsersBo(
                (long) customers.size(),
                (long) smes.size(),
                (long) agents.size()),
                AdminConstants.TOTAL_USERS);
    }

    public Response allApprovalRequests() {
        log.info("Invoked -+- AdminService -+- approvalRequests()");
        List<VerificationStatus> verificationStatuses = this.verificationStatusRepository.findByRoleIn(
                Sort.by(Sort.Direction.DESC, "createdDate"), Arrays.asList(RoleConstants.SME, RoleConstants.AGENT));
        return new Response(this.verificationStatusToBo(verificationStatuses), AdminConstants.APPROVAL_REQUESTS);
    }

    private Stream verificationStatusToBo(List<VerificationStatus> verificationStatuses) {
        return verificationStatuses.stream().map(verificationStatus -> new VerificationStatusBo(verificationStatus,
                null,
                verificationStatus.getRole().equals(RoleConstants.SME) ? this.toSmeBo(this.smeRepository.getByUserId(verificationStatus.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException(AdminConstants.NO_SME_FOUND))) : null,
                verificationStatus.getRole().equals(RoleConstants.AGENT) ? this.toAgentBo(this.agentRepository.getByUserId(verificationStatus.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException(AdminConstants.NO_AGENT_FOUND))) : null));
    }


    public Response approve(ApprovalPayload approvalPayload) {
        log.info("Invoked -+- AdminService -+- approve()");
        VerificationStatus verificationStatus = this.verificationStatusRepository.findById(approvalPayload.getId()).orElseThrow(() -> new ResourceNotFoundException(AdminConstants.NO_APPROVAL_STATUS + approvalPayload.getId()));
        /*if (!verificationStatus.getCreatedDate().isEqual(verificationStatus.getModificationDate()) || verificationStatus.getStatus() == VerificationStatusConstants.ACCEPTED) {
            throw new BadRequestException(AdminConstants.APPROVAL_STATUS);
        }*/
        verificationStatus.setStatus(approvalPayload.isAction() ? VerificationStatusConstants.ACCEPTED : VerificationStatusConstants.DECLINED);
        verificationStatus.setUpdatedBy(this.authenticationService.currentUser());
        this.verificationStatusRepository.saveAndFlush(verificationStatus);
        pushNotificationService.sendNotificationToCustomerApproval(verificationStatus);
        return new Response<>(approvalPayload.isAction() ? AdminConstants.REQUEST_APPROVED : AdminConstants.REQUEST_DECLINED, "Successful");
    }

  /*  public Response userVerificationRequests(String userId) {
        log.info("Invoked -+- AdminService -+- userVerificationRequests()");
        return new Response<>(this.verificationStatusToBo(this.verificationStatusRepository
                .findByUser(this.userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException(AdminConstants.NO_USER_FOUND_WITH_ID + userId)))),
                AdminConstants.APPROVAL_REQUESTS);
    }*/

    /*@Transactional(propagation = Propagation.REQUIRED)
    public Response deleteApproval(String verificationRequestId) {
        log.info("Invoked -+- AdminService -+- deleteApproval()");
        VerificationStatus verificationStatus = this.verificationStatusRepository.findById(verificationRequestId)
                .orElseThrow(() -> new ResourceNotFoundException(AdminConstants.NO_APPROVAL_STATUS + verificationRequestId));
        verificationStatus.getUser().getRoles().remove(this.roleRepository.findByRole(verificationStatus.getRole().getValue()));
        if (verificationStatus.getRole() == RoleConstants.SME) {
            this.smeRepository.deleteByUserId(verificationStatus.getUser().getId());
        } else if (verificationStatus.getRole() == RoleConstants.AGENT) {
            this.agentRepository.deleteByUserId(verificationStatus.getUser().getId());
        } else {
            throw new BadRequestException(AdminConstants.INVALID_REQUEST);
        }
        this.userRepository.saveAndFlush(verificationStatus.getUser());
        this.verificationStatusRepository.deleteById(verificationRequestId);
        return new Response(true, AdminConstants.APPROVAL_REQUEST_DELETED);
    }*/

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


   /* public ListResponseWithCount<OrderInfoBo> listOrdersByDate(OrderDto orderDto) {

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
    }*/

    private Sort sortBy(PaginationBo paginationBo) {
        if (this.helper.notNullAndBlank(paginationBo.getSortKey()) && helper.notNullAndBlank(paginationBo.getSortOrder())) {
            if (paginationBo.getSortOrder().equals(OrderBy.DESC.name())) {

                return Sort.by(paginationBo.getSortKey()).descending();
            } else {
                return Sort.by(paginationBo.getSortKey()).ascending();
            }
        }
        return Sort.by("created_At").descending();
    }

    public Response<AdminPaginationDto> userSearch(PaginationBo paginationBo) {
        log.info("Invoked -+- AdminService -+- userSearch");
        Page<User> page = null;
        Sort sort = this.sortBy(paginationBo);
        if (this.helper.notNullAndBlank(paginationBo.getSearchText())) {
            page = this.userRepository.searchUsers(PageRequest.of(paginationBo.getPageNumber(), pageSize, sort), paginationBo.getSearchText().trim(), false);
        } else {
           return new Response<>();
        }
        List<UserBo> userBos = new ArrayList<>();
        page.get().forEach(user -> {
            userBos.add(new UserBo(user));
        });
        return new Response<>(new AdminPaginationDto<>(userBos, page.getTotalElements(), paginationBo.getPageNumber(),
                page.getTotalPages()),"");
    }
}