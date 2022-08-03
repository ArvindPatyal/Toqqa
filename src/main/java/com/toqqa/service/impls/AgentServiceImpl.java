package com.toqqa.service.impls;

import com.toqqa.bo.AgentBo;
import com.toqqa.bo.AgentReferralBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.bo.UserBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.constants.RoleConstants;
import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.*;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ResourceCreateUpdateException;
import com.toqqa.payload.AgentPayload;
import com.toqqa.payload.AgentRegistration;
import com.toqqa.payload.AgentUpdate;
import com.toqqa.payload.Response;
import com.toqqa.repository.AgentRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.AgentService;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.StorageService;
import com.toqqa.service.VerificationStatusService;
import com.toqqa.util.Constants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepo;

    @Autowired
    private SmeRepository smeRepo;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private StorageService storageService;
    @Autowired
    private Helper helper;

    @Autowired
    private VerificationStatusService statusService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AgentBo agentRegistration(AgentRegistration agentRegistration, String userId, boolean isNewUser) {
        log.info("Invoked :: AgentServiceImpl :: agentRegistration()");
        if (!this.alreadyAgent(userId)) {
            try {
                Agent agent = new Agent();
                agent.setUserId(userId);
                agent.setAgentId(Constants.AGENT_CONSTANT + this.helper.numericString(6));
                User user = this.userRepo.findById(userId).get();
                List<Role> roles = new ArrayList<>();
                roles.addAll(user.getRoles());
                roles.add(this.roleRepo.findByRole(RoleConstants.AGENT.getValue()));
                user.setRoles(roles);
                try {
                    agent.setIdProof(this.storageService.uploadFileAsync(agentRegistration.getIdProof(), userId, FolderConstants.DOCUMENTS.getValue()).get());
                    agent.setAgentDocuments(this.storageService.uploadFileAsync(agentRegistration.getAgentDocuments(), userId, FolderConstants.DOCUMENTS.getValue()).get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new ResourceCreateUpdateException("Cannot upload IdProof OR Documents");
                }
                agent = this.agentRepo.saveAndFlush(agent);
                this.userRepo.saveAndFlush(user);
                AgentBo bo = new AgentBo(agent);
                bo.setAgentDocuments(this.helper.prepareResource(agent.getAgentDocuments()));
                bo.setIdProof(this.helper.prepareResource(agent.getIdProof()));

                VerificationStatus status = new VerificationStatus();
                status.setStatus(VerificationStatusConstants.PENDING);
                status.setUser(user);
                status.setRole(RoleConstants.AGENT);
                this.statusService.createVerificationStatus(status);

                return bo;
            } catch (Exception e) {
                log.error("Invoked :: AgentServiceImpl :: agentRegistration() :: Unable to create Agent", e);
                if (isNewUser == true) {
                    this.userRepo.deleteById(userId);
                }
            }
        }
        throw new BadRequestException("user already an agent");
    }


    private Boolean alreadyAgent(String id) {
        log.info("Invoked :: AgentServiceImpl :: alreadyAgent()");
        User user = this.userRepo.findById(id).get();
        return user.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleConstants.AGENT.getValue()));
    }

    @Override
    public AgentBo agentUpdate(AgentUpdate agentUpdatePayload) {
        log.info("Invoked :: AgentServiceImpl :: agentUpdate()");
        User user = this.authenticationService.currentUser();
        Agent agent = this.agentRepo.findByUserId(user.getId());
        if (agent == null) {
            throw new BadRequestException("You are not an agent");
        }
        if (agentUpdatePayload.getAgentDocuments() != null) {
            try {
                agent.setAgentDocuments(this.storageService.uploadFileAsync(agentUpdatePayload.getAgentDocuments(), agent.getUserId(), FolderConstants.DOCUMENTS.getValue()).get());
            } catch (InterruptedException | ExecutionException e) {
                throw new ResourceCreateUpdateException("Cannot update agent,document operations failed");
            }
        }
       /* if (agentUpdatePayload.getIdProof().isEmpty() && agentUpdatePayload.getIdProof() == null) {
            try {
                agent.setIdProof(this.storageService.uploadFileAsync(agentUpdatePayload.getIdProof(), agent.getUserId(), FolderConstants.DOCUMENTS.getValue()).get());
            } catch (InterruptedException | ExecutionException e) {
                throw new ResourceCreateUpdateException("Cannot update agent,idProof  operations failed");
            }
        }*/
        if (agentUpdatePayload.getAgentProfilePicture() != null) {
            try {
                agent.setAgentProfilePicture(this.storageService.uploadFileAsync(agentUpdatePayload.getAgentProfilePicture(), agent.getUserId(), FolderConstants.PROFILE_PICTURE.getValue()).get());
            } catch (InterruptedException | ExecutionException e) {
                throw new ResourceCreateUpdateException("Cannot update agent,profilePicture  operations failed");
            }
        }
        agent = this.agentRepo.saveAndFlush(agent);
        AgentBo bo = new AgentBo(agent);
        bo.setAgentDocuments(this.helper.prepareResource(agent.getAgentDocuments()));
        bo.setIdProof(this.helper.prepareResource(agent.getIdProof()));
        bo.setProfilePicture(this.helper.prepareResource(agent.getAgentProfilePicture()));
        return bo;
    }


    @Override
    public AgentBo fetchAgent(String id) {
        log.info("Invoked :: AgentServiceImpl :: fetchAgent()");
        Agent agent = this.agentRepo.findByUserId(id);
        if (agent != null) {
            AgentBo bo = new AgentBo(agent);
            bo.setAgentDocuments(this.helper.prepareResource(agent.getAgentDocuments()));
            bo.setIdProof(this.helper.prepareResource(agent.getIdProof()));
            bo.setAgentId(agent.getAgentId());
            return bo;
        }
        throw new BadRequestException("no Agent found with id= " + id);
    }

    @Override
    public Response fetchAgentReferralList(AgentPayload agentPayload) {
        log.info("Invoked :: AgentServiceImpl :: fetchAgentReferenceList()");
        User user = this.authenticationService.currentUser();
        Role agentRole = this.roleRepo.findByRole(RoleConstants.AGENT.getValue());
        Role smeRole = this.roleRepo.findByRole(RoleConstants.SME.getValue());
        List<Role> roles = agentPayload.getRoles().stream().map(roleConstants -> this.roleRepo.findByRole(roleConstants.getValue())).collect(Collectors.toList());
        Agent agent = this.agentRepo.findByUserId(user.getId());
        List<User> userList = this.userRepo.findByAgentIdAndRolesIn(agent.getAgentId(), roles);
        List<AgentReferralBo> agentReferralBo = new ArrayList<>();
        for (User users : userList) {
            String userId = users.getId();
            UserBo userBo = new UserBo(users);
            userBo.setProfilePicture(this.helper.prepareResource(userBo.getProfilePicture()));
            userBo.setCreatedAt(user.getCreatedAt());

            SmeBo smeBo = null;
            AgentBo agentBo = null;

            for (Role role : users.getRoles()) {
                if (role.equals(smeRole)) {
                    Sme sme = this.smeRepo.findByUserId(userId);
                    if (sme != null) {
                        smeBo = new SmeBo(sme);
                        smeBo.setBusinessLogo(this.helper.prepareResource(smeBo.getBusinessLogo()));
                    }
                }
                if (role.equals(agentRole)) {
                    Agent agents = this.agentRepo.findByUserId(userId);
                    if (agents != null) {
                        agentBo = new AgentBo(agents);
                        agentBo.setIdProof(this.helper.prepareResource(agentBo.getIdProof()));
                        agentBo.setAgentDocuments(this.helper.prepareResource(agentBo.getAgentDocuments()));
                    }
                }
            }
            agentReferralBo.add(new AgentReferralBo(userBo, smeBo, agentBo));
        }
        return new Response(agentReferralBo, "Agent Referral List Returned");
    }

    @Override
    public AgentBo becomeAnAgent(AgentRegistration agentRegistration) {
        log.info("Invoked :: AgentServiceImpl ::becomeAnAgent()");
        User user = this.authenticationService.currentUser();
        return this.agentRegistration(agentRegistration, user.getId(), false);
    }
}

