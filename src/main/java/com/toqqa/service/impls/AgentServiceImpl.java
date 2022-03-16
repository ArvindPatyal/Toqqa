package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.toqqa.util.Helper;
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
import com.toqqa.payload.AgentUpdate;
import com.toqqa.repository.AgentRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.AgentService;
import com.toqqa.service.StorageService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	@Autowired
	private Helper helper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AgentBo agentRegistration(AgentRegistration agentRegistration, String userId) {
		log.info("Inside agent registration");
		if (!this.alreadyAgent(userId)) {
			try {
				Agent agent = new Agent();
				agent.setUserId(userId);
				User user = this.userRepo.findById(userId).get();
				List<Role> roles = new ArrayList<>();
				roles.addAll(user.getRoles());
				roles.add(this.roleRepo.findByRole(RoleConstants.AGENT.getValue()));
				user.setRoles(roles);
				try {
					agent.setIdProof(this.storageService
							.uploadFileAsync(agentRegistration.getIdProof(), userId, FolderConstants.DOCUMENTS.getValue())
							.get());
					agent.setAgentDocuments(this.storageService.uploadFileAsync(agentRegistration.getAgentDocuments(),
							userId, FolderConstants.DOCUMENTS.getValue()).get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				this.userRepo.saveAndFlush(user);

				agent = this.agentRepo.saveAndFlush(agent);
				agent.setAgentDocuments(this.prepareResource(agent.getAgentDocuments()));
				agent.setIdProof(this.prepareResource(agent.getIdProof()));
				return new AgentBo(agent);
			}
			catch (Exception e){
				log.error("unable to create agent",e);
				this.userRepo.deleteById(userId);
			}
		}
		throw new BadRequestException("user already an agent");
	}

	private String prepareResource(String location){
		if(this.helper.notNullAndBlank(location)){
			return this.storageService.buildResourceString(location);
		}
		return "";
	}

	private Boolean alreadyAgent(String id) {
		log.info("Inside  already agent");
		User user = this.userRepo.findById(id).get();
		return user.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleConstants.AGENT.getValue()));
	}

	@Override
	public AgentBo agentUpdate(AgentUpdate payload) {
		log.info("Inside  agent Update");
		Agent agent = this.agentRepo.findById(payload.getAgentId()).get();

		try {
			agent.setAgentDocuments(this.storageService.uploadFileAsync(payload.getAgentDocuments(), agent.getUserId(),
					FolderConstants.DOCUMENTS.getValue()).get());
			agent.setIdProof(this.storageService
					.uploadFileAsync(payload.getIdProof(), agent.getUserId(), FolderConstants.DOCUMENTS.getValue())
					.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		agent = this.agentRepo.saveAndFlush(agent);
		agent.setAgentDocuments(this.prepareResource(agent.getAgentDocuments()));
		agent.setIdProof(this.prepareResource(agent.getIdProof()));
		return new AgentBo(agent);

	}

	@Override
	public AgentBo fetchAgent(String id) {
		log.info("Inside fetch Agent");
		Agent agent = this.agentRepo.findById(id).get();
		if (agent!=null) {
			agent.setAgentDocuments(this.prepareResource(agent.getAgentDocuments()));
			agent.setIdProof(this.prepareResource(agent.getIdProof()));
			return new AgentBo(agent);
		}
		throw new BadRequestException("no user found with id= " + id);
	}
}
