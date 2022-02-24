package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toqqa.bo.SmeBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.Role;
import com.toqqa.domain.Sme;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.SmeRegistration;
import com.toqqa.repository.CategoryRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.repository.SubcategoryRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.SmeService;
import com.toqqa.service.StorageService;

@Service
public class SmeServiceImpl implements SmeService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubcategoryRepository subcategoryRepository;

	@Autowired
	private SmeRepository smeRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private StorageService storageService;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SmeBo smeRegistration(SmeRegistration smeRegistration, String userId) {
		if (!alreadySme(userId)) {
			Sme sme = new Sme();
			sme.setNameOfBusiness(smeRegistration.getNameOfBusiness());
			sme.setBusinessAddress(smeRegistration.getBusinessAddress());
			sme.setState(smeRegistration.getState());
			sme.setCountry(smeRegistration.getCountry());
			sme.setTypeOfBusiness(smeRegistration.getTypeOfBusiness());
			sme.setDeliveryRadius(smeRegistration.getDeliveryRadius());
			sme.setDeliveryCharges(smeRegistration.getDeliveryCharge());
			sme.setIsDeleted(false);
			sme.setDescription(smeRegistration.getDescription());
			sme.setCity(smeRegistration.getCity());
			sme.setIsDeliverToCustomer(smeRegistration.getDeliverToCustomer());
			sme.setIsRegisterWithGovt(smeRegistration.getIsRegisteredWithGovt());
			if(smeRegistration.getStartTimeOfDelivery()!=null&&smeRegistration.getEndTimeOfDelivery()!=null) {
				sme.setStartTimeOfDelivery(new Date(smeRegistration.getStartTimeOfDelivery()));
				sme.setEndTimeOfDelivery(new Date(smeRegistration.getEndTimeOfDelivery()));
			}
			sme.setUserId(userId);

			sme.setBusinessCatagory(this.categoryRepository.findAllById(smeRegistration.getBusinessCategory()));
			sme.setBusinessSubCatagory(
					this.subcategoryRepository.findAllById(smeRegistration.getBusinessSubCategory()));
			User user = this.userRepo.findById(userId).get();
			List<Role> roles = new ArrayList<Role>();
			roles.addAll(user.getRoles());
			roles.add(this.roleRepo.findByRole(RoleConstants.SME.getValue()));
			user.setRoles(roles);
			user = this.userRepo.saveAndFlush(user);

			// TODO upload functionality
			try {
				if(smeRegistration.getIdProof()!=null&&!smeRegistration.getIdProof().isEmpty()) {
					sme.setIdProof(this.storageService.uploadFileAsync(smeRegistration.getIdProof(), userId, FolderConstants.DOCUMENTS.getValue()).get());
				}
				if(smeRegistration.getBusinessLogo()!=null&&!smeRegistration.getBusinessLogo().isEmpty()) {
					sme.setBusinessLogo(this.storageService.uploadFileAsync(smeRegistration.getBusinessLogo(), userId, FolderConstants.LOGO.getValue()).get());
				}
				if(smeRegistration.getRegDoc()!=null&&!smeRegistration.getRegDoc().isEmpty()) {
					sme.setRegDoc(this.storageService.uploadFileAsync(smeRegistration.getRegDoc(), userId, FolderConstants.DOCUMENTS.getValue()).get());
				}
			} catch (InterruptedException | ExecutionException  e) {
				e.printStackTrace();
			} 					

			sme = this.smeRepo.saveAndFlush(sme);
			return new SmeBo(sme);
		}
		throw new BadRequestException("user already sme");
	}

	private Boolean alreadySme(String id) {
		User user = this.userRepo.findById(id).get();
		return user.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleConstants.SME.getValue()));
	}

}
