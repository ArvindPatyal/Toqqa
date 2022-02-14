package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.toqqa.bo.SmeBo;
import com.toqqa.domain.Role;
import com.toqqa.domain.Sme;
import com.toqqa.domain.User;
import com.toqqa.payload.SmeSignUp;
import com.toqqa.repository.CategoryRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.repository.SubcategoryRepository;
import com.toqqa.repository.UserRepository;
import com.toqqa.service.SmeService;

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
	public SmeBo addSme(SmeSignUp smeSignUp) {
		Sme sme = new Sme();
		sme.setNameOfBusiness(smeSignUp.getNameOfBusiness());
		sme.setBusinessAddress(smeSignUp.getBusinessAddress());
		sme.setState(smeSignUp.getState());
		sme.setCountry(smeSignUp.getCountry());
		sme.setTypeOfBusiness(smeSignUp.getTypeOfBusiness());
		sme.setDeliveryRadius(smeSignUp.getDeliveryRadius());
		sme.setDeliveryCharges(smeSignUp.getDeliveryCharge());
		sme.setUserId(smeSignUp.getUserId());

		sme.setBusinessCatagory(this.categoryRepository.findAllById(smeSignUp.getBusinessCategory()));
		sme.setBusinessSubCatagory(this.subcategoryRepository.findAllById(smeSignUp.getBusinessSubCategory()));
		User user = this.userRepo.findById(smeSignUp.getUserId()).get();
		List<Role> roles = new ArrayList<Role>();
		roles = user.getRoles();
		roles.add(this.roleRepo.findByRole(smeSignUp.getUserId()));
		user.setRoles(roles);
		user = this.userRepo.saveAndFlush(user);

		// TODO upload functionality
		sme.setRegDoc("smeSignUp.getRegDoc()");
		sme.setIdProof("smeSignUp.getIdProof()");
		sme.setBusinessLogo("smeSignUp.getBusinessLogo()");

		sme = this.smeRepo.saveAndFlush(sme);
		return new SmeBo(sme);

	}

}
