package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toqqa.bo.AgentBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.Agent;
import com.toqqa.domain.Role;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.AgentRegistration;
import com.toqqa.repository.AgentRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.AgentService;
import com.toqqa.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentRepository agentRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private StorageService storageService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AgentBo agentRegistration(AgentRegistration agentRegistration, String userId) {
		log.info("Inside agent registration");
		if (!this.alreadyAgent(userId)) {
			Agent agent = new Agent();

			
			agent.setUserId(userId);
			User user = this.userRepo.findById(userId).get();
			List<Role> roles = new ArrayList<>();
			roles.addAll(user.getRoles());
			roles.add(this.roleRepo.findByRole(RoleConstants.AGENT.getValue()));
			user.setRoles(roles);
			try {
				agent.setIdProof(this.storageService.uploadFileAsync(agentRegistration.getIdProof(), userId, FolderConstants.DOCUMENTS.getValue()).get());
				agent.setAgentDocuments(this.storageService.uploadFileAsync(agentRegistration.getAgentDocuments(), userId, FolderConstants.DOCUMENTS.getValue()).get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			this.userRepo.saveAndFlush(user);
			agent = this.agentRepo.saveAndFlush(agent);
			return new AgentBo(agent);
		}
		throw new BadRequestException("user already an agent");
	}

	private Boolean alreadyAgent(String id) {
		log.info("Inside  already agent");
		User user = this.userRepo.findById(id).get();
		return user.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleConstants.AGENT.getValue()));
	}
}
