package com.toqqa.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.payload.AddProduct;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepo;

	@Override
	public ProductBo addProduct(AddProduct addProduct) {

		Product product = new Product();

		product.setProductName(addProduct.getProductName());
		product.setCategory(addProduct.getCategory());
		product.setSubCategory(addProduct.getSubCategory());
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

		product = this.productRepo.saveAndFlush(product);

		return new ProductBo(product);
	}

}
