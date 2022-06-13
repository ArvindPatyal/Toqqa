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
import com.toqqa.domain.ProductCategory;
import com.toqqa.domain.ProductSubCategory;
import com.toqqa.domain.Role;
import com.toqqa.domain.SubCategory;
import com.toqqa.repository.CategoryRepository;
import com.toqqa.repository.ProductCategoryRepository;
import com.toqqa.repository.ProductSubCategoryRepository;
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
	@Autowired
	private ProductCategoryRepository productCategoryRepo;
	@Autowired
	private ProductSubCategoryRepository productSubCategoryRepo;

	@Override
	public void run(String... args) throws Exception {
		this.initRoles();
		this.initCategories();
		this.initSubcategories();
		this.initProductcategories();
		this.initProductSubCategories();
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
				RoleConstants.SME.getValue(), RoleConstants.ADMIN.getValue());
		roles.forEach(role -> {
			if (this.roleRepository.findByRole(role) == null) {
				Role r = new Role(null, role, null);
				this.roleRepository.saveAndFlush(r);
			}
		});
	}

	private void initProductcategories() {
		List<String> productCategories = Arrays.asList("Dining", "Groceries and Food", "Cleaning Supplies", "Weavers",
				"Jewellery", "Fashion", "Health and Beauty", "Home and Lifestyle", "Oonery, Gift Cards and Vouchers",
				"Toys", "Financial Services", "Outdoor, Fitness and Sport", "Other");
		productCategories.forEach(productcategory -> {
			if (this.productCategoryRepo.findByProductCategory(productcategory) == null) {
				ProductCategory pc = new ProductCategory();
				pc.setProductCategory(productcategory);
				this.productCategoryRepo.saveAndFlush(pc);
			}
		});
	}

	private void initProductSubCategories() {
		Map<String, List<String>> map = new LinkedHashMap<>();
		map.put("Dining",
				Arrays.asList("Restaurant-Eat in", "Restaurant-Eat in and Take away",
						"Restaurant-Eat in, Take away and Delivery", "Cafe-Eat in", "Cafe-Eat in and Takeaway",
						"Cafe-Eat in , Takeaway and Delivery", "Cafe-Takeaway", "Cafe Takeaway and Delivery",
						"Street Food-Takeaway", "Street Food-Takeaway and Delivery"));
		map.put("Groceries and Food", Arrays.asList("Fresh", "Staples", "Babies", "Pets", "Bakery", "Others"));
		map.put("Cleaning Supplies", Arrays.asList());
		map.put("Weavers", Arrays.asList("Cloth", "Homewares", "Rugs/Carpets"));
		map.put("Jewellery", Arrays.asList("Handmade", "Retail"));
		map.put("Fashion", Arrays.asList("Men", "Women", "Children and Infants"));
		map.put("Home & Lifestyle", Arrays.asList());
		map.put("Health and Beauty", Arrays.asList("For Men", "For Women"));
		map.put("Stationery, Gift Cards and Vouchers", Arrays.asList());
		map.put("Toys", Arrays.asList());
		map.put("Financial Services", Arrays.asList());
		map.put("Outdoor, Fitness and Sport", Arrays.asList());
		map.put("Other", Arrays.asList());
		map.forEach((productcategory, productsubcategories) -> {
			ProductCategory pc = this.productCategoryRepo.findByProductCategory(productcategory);
			if (pc != null) {
				productsubcategories.forEach(productsubcategory -> {
					if (this.productSubCategoryRepo.findByProductSubCategory(productsubcategory) == null) {
						ProductSubCategory psc = new ProductSubCategory();
						psc.setProductCategory(pc);
						psc.setProductSubCategory(productsubcategory);
						this.productSubCategoryRepo.saveAndFlush(psc);
					}
				});
			}
		});
	}

}
