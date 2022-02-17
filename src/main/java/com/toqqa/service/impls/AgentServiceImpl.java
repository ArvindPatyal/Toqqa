package com.toqqa.service.impls;

import com.toqqa.bo.AgentBo;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.Agent;
import com.toqqa.domain.Role;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.AgentSignUp;
import com.toqqa.repository.AgentRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public AgentBo addAgent(AgentSignUp agentSignUp) {
        if (!this.alreadyAgent(agentSignUp.getUserId())) {
            Agent agent = new Agent();

            agent.setAgentDocuments("agentSignUp.getAgentDocuments()");
            agent.setIdProof("agentSignUp.getIdProof()");
            agent.setUserId(agentSignUp.getUserId());
            User user = this.userRepo.findById(agentSignUp.getUserId()).get();
            List<Role> roles = user.getRoles();
            roles.add(this.roleRepo.findByRole(RoleConstants.AGENT.getValue()));
            this.userRepo.saveAndFlush(user);
            agent = this.agentRepo.saveAndFlush(agent);

            return new AgentBo(agent);
        }
        throw new BadRequestException("user already an agent");
    }

    private Boolean alreadyAgent(String id) {
        User user = this.userRepo.findById(id).get();
        List<Role> roles = user.getRoles();
        return user.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleConstants.AGENT.getValue()));
     }
}
