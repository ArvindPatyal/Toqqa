package com.toqqa.eventListner;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.toqqa.constants.RoleConstants;
import com.toqqa.domain.Category;
import com.toqqa.domain.Role;
import com.toqqa.domain.SubCategory;
import com.toqqa.repository.CategoryRepository;
import com.toqqa.repository.RoleRepository;
import com.toqqa.repository.SubcategoryRepository;

@Configuration
public class ApplicationStartupEvent implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public void run(String... args) throws Exception {
		this.initRoles();
		this.initCategories();
		this.initSubcategories();
	}

	private void initCategories() {
		List<String> categories = Arrays.asList("Farmer", "Manufacturer", "Distributor", "Retailer");
		categories.forEach(category -> {
			if (this.categoryRepository.findByCategory(category) == null) {
				Category c = new Category();
				c.setCategory(category);
				this.categoryRepository.saveAndFlush(c);
			}
		});
	}

	private void initSubcategories() {
		Map<String, List<String>> map = new LinkedHashMap<>();
		map.put("Farmer", Arrays.asList("Food Cooperatives", "Green Grocers"));
		map.put("Manufacturer", Arrays.asList("Dining", "Weavers", "Jewellers", "Fashion", "Health and Beauty",
				"Home and Lifestyle", "Sports", "Outdoor", "Gym and Fitness", "Toys"));
		map.forEach((category, subcategories) -> {
			Category c = this.categoryRepository.findByCategory(category);
			if (c != null) {
				subcategories.forEach(subcategory -> {
					if (this.subcategoryRepository.findBySubcategory(subcategory) == null) {
						SubCategory sc = new SubCategory();
						sc.setCategory(c);
						sc.setSubcategory(subcategory);
						this.subcategoryRepository.saveAndFlush(sc);
					}
				});
			}
		});

	}

	private void initRoles() {
		List<String> roles = Arrays.asList(RoleConstants.AGENT.getValue(), RoleConstants.CUSTOMER.getValue(),
				RoleConstants.SME.getValue());
		roles.forEach(role -> {
			if (this.roleRepository.findByRole(role) == null) {
				Role r = new Role(null, role);
				this.roleRepository.saveAndFlush(r);
			}
		});
	}
}
