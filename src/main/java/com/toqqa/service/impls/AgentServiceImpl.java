package com.toqqa.service.impls;

import com.toqqa.bo.AgentBo;
import com.toqqa.bo.AgentReferralBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.bo.UserBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.Agent;
import com.toqqa.domain.Role;
import com.toqqa.domain.Sme;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
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
import com.toqqa.util.Constants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AgentBo agentRegistration(AgentRegistration agentRegistration, String userId) {
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
                    e.printStackTrace();
                }

                this.userRepo.saveAndFlush(user);

                agent = this.agentRepo.saveAndFlush(agent);

                AgentBo bo = new AgentBo(agent);
                bo.setAgentDocuments(this.prepareResource(agent.getAgentDocuments()));
                bo.setIdProof(this.prepareResource(agent.getIdProof()));
                return bo;
            } catch (Exception e) {
                log.error("Invoked :: AgentServiceImpl :: agentRegistration() :: Unable to create Agent", e);
                this.userRepo.deleteById(userId);
            }
        }
        throw new BadRequestException("user already an agent");
    }

    private String prepareResource(String location) {
        log.info("Invoked :: AgentServiceImpl :: prepareResource()");
        if (this.helper.notNullAndBlank(location)) {
            return this.storageService.generatePresignedUrl(location);
        }
        return "";
    }

    private Boolean alreadyAgent(String id) {
        log.info("Invoked :: AgentServiceImpl :: alreadyAgent");
        User user = this.userRepo.findById(id).get();
        return user.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleConstants.AGENT.getValue()));
    }

    @Override
    public AgentBo agentUpdate(AgentUpdate payload) {
        log.info("Invoked :: AgentServiceImpl :: agentUpdate()");
        Agent agent = this.agentRepo.findById(payload.getAgentId()).get();

        try {
            agent.setAgentDocuments(this.storageService.uploadFileAsync(payload.getAgentDocuments(), agent.getUserId(), FolderConstants.DOCUMENTS.getValue()).get());
            agent.setIdProof(this.storageService.uploadFileAsync(payload.getIdProof(), agent.getUserId(), FolderConstants.DOCUMENTS.getValue()).get());
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        agent = this.agentRepo.saveAndFlush(agent);
        AgentBo bo = new AgentBo(agent);
        bo.setAgentDocuments(this.prepareResource(agent.getAgentDocuments()));
        bo.setIdProof(this.prepareResource(agent.getIdProof()));
        return bo;
    }

    @Override
    public AgentBo fetchAgent(String id) {
        log.info("Invoked :: AgentServiceImpl :: fetchAgent()");
        Agent agent = this.agentRepo.findByUserId(id);
        if (agent != null) {
            AgentBo bo = new AgentBo(agent);
            bo.setAgentDocuments(this.prepareResource(agent.getAgentDocuments()));
            bo.setIdProof(this.prepareResource(agent.getIdProof()));
            return bo;
        }
        throw new BadRequestException("no user found with id= " + id);
    }

    @Override
    public Response fetchAgentReferralList(AgentPayload agentPayload) {
        log.info("Invoked :: AgentServiceImpl :: fetchAgentReferenceList()");
        User user = this.authenticationService.currentUser();
        Role agentRole = this.roleRepo.findByRole(RoleConstants.AGENT.getValue());
        Role smeRole = this.roleRepo.findByRole(RoleConstants.SME.getValue());
        if (!user.getRoles().stream().anyMatch(role -> role.equals(agentRole))) {
            throw new BadRequestException("you are not an agent");
        }
        Agent agent = this.agentRepo.findByUserId(user.getId());
        List<User> userList = this.userRepo.findByAgentId(agent.getAgentId());
        List<AgentReferralBo> agentReferralBo = new ArrayList<>();
        for (User users : userList) {
            String userId = users.getId();
            UserBo userBo = new UserBo(users);
            userBo.setProfilePicture(this.helper.prepareResource(userBo.getProfilePicture()));
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
}

