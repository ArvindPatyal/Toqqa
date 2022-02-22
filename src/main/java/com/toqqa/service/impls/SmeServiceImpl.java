package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;

import com.toqqa.constants.RoleConstants;
import com.toqqa.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.toqqa.bo.SmeBo;
import com.toqqa.domain.Role;
import com.toqqa.domain.Sme;
import com.toqqa.domain.User;
import com.toqqa.payload.SmeRegistration;
import com.toqqa.repository.CategoryRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.repository.SubcategoryRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.SmeService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SmeBo smeRegistration(SmeRegistration smeRegistration,String userId) {
		if(!alreadySme(userId)) {
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
			sme.setTimeOfDelivery(smeRegistration.getTimeOfDelivery());
			sme.setUserId(userId);

			sme.setBusinessCatagory(this.categoryRepository.findAllById(smeRegistration.getBusinessCategory()));
			sme.setBusinessSubCatagory(this.subcategoryRepository.findAllById(smeRegistration.getBusinessSubCategory()));
			User user = this.userRepo.findById(userId).get();
			List<Role> roles = new ArrayList<Role>();
			roles.addAll(user.getRoles());
			roles.add(this.roleRepo.findByRole(RoleConstants.SME.getValue()));
			user.setRoles(roles);
			user = this.userRepo.saveAndFlush(user);

			// TODO upload functionality
			sme.setRegDoc("smeSignUp.getRegDoc()");
			sme.setIdProof("smeSignUp.getIdProof()");
			sme.setBusinessLogo("smeSignUp.getBusinessLogo()");

			sme = this.smeRepo.saveAndFlush(sme);
			return new SmeBo(sme);
		}
		throw new BadRequestException("user already sme");
	}

	private Boolean alreadySme(String id){
		User user = this.userRepo.findById(id).get();
		return user.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleConstants.SME.getValue()));
	}

}
