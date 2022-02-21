package com.toqqa.service.impls;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.UpdateProduct;
import com.toqqa.repository.ProductCategoryRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.ProductSubCategoryRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	ProductCategoryRepository productCategoryRepo;

	@Autowired
	ProductSubCategoryRepository productSubCategoryRepo;

	@Autowired
	private AuthenticationService authenticationService;


	@Override
	public ProductBo addProduct(AddProduct addProduct) {

		Product product = new Product();

		product.setProductName(addProduct.getProductName());
		product.setProductCategories(this.productCategoryRepo.findAllById(addProduct.getProductCategory()));
		product.setProductSubCategories(this.productSubCategoryRepo.findAllById(addProduct.getProductSubCategory()));
		product.setImage("addProduct.getImage()");
		product.setDescription(addProduct.getDescription());
		product.setDetails(addProduct.getDetails());
		product.setUnitsInStock(addProduct.getUnitsInStock());
		product.setPricePerUnit(addProduct.getPricePerUnit());
		product.setDiscount(addProduct.getDiscount());
		product.setMaximumUitsInOneOrder(addProduct.getMaximumUnitsInOneOrder());
		product.setMinimumUnitsInOneOrder(addProduct.getMinimumUnitsInOneOrder());
		product.setExpiryDate(addProduct.getExpiryDate());
		product.setCountryOfOrigin(addProduct.getCountryOfOrigin());
		product.setManufacturerName(addProduct.getManufacturerName());
		product.setUser(authenticationService.currentUser());

		product = this.productRepo.saveAndFlush(product);

		return new ProductBo(product);
	}

	@Override
	public ProductBo updateProduct(UpdateProduct updateProduct) {

		Product product = new Product();

		product.setId(updateProduct.getProductId());
		product.setProductName(updateProduct.getProductName());
		product.setProductCategories(this.productCategoryRepo.findAllById(updateProduct.getProductCategory()));
		product.setProductSubCategories(this.productSubCategoryRepo.findAllById(updateProduct.getProductSubCategory()));
		product.setImage("updateProduct.getImage()");
		product.setDescription(updateProduct.getDescription());
		product.setDetails(updateProduct.getDetails());
		product.setUnitsInStock(updateProduct.getUnitsInStock());
		product.setPricePerUnit(updateProduct.getPricePerUnit());
		product.setDiscount(updateProduct.getDiscount());
		product.setMaximumUitsInOneOrder(updateProduct.getMaximumUnitsInOneOrder());
		product.setMinimumUnitsInOneOrder(updateProduct.getMinimumUnitsInOneOrder());
		product.setExpiryDate(updateProduct.getExpiryDate());
		product.setCountryOfOrigin(updateProduct.getCountryOfOrigin());
		product.setManufacturerName(updateProduct.getManufacturerName());

		product = this.productRepo.saveAndFlush(product);

		return new ProductBo(product);

	}

	@Override
	public ProductBo fetchProduct(String id) {

		Optional<Product> product = this.productRepo.findById(id);
		if (product.isPresent()) {
			return new ProductBo(product.get());
		}
		throw new BadRequestException("no product found with id= " + id);
	}

}
