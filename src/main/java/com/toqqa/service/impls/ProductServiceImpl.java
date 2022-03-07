package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.domain.Product;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.UpdateProduct;
import com.toqqa.repository.ProductCategoryRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.ProductSubCategoryRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.ProductService;
import com.toqqa.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	ProductCategoryRepository productCategoryRepo;

	@Autowired
	ProductSubCategoryRepository productSubCategoryRepo;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private StorageService storageService;

	@Value("${pageSize}")
	private Integer pageSize;
	
	@Override
	public ProductBo addProduct(AddProduct addProduct) {
		log.info("Inside Add Product");
		Product product = new Product();

		product.setProductName(addProduct.getProductName());
		product.setProductCategories(this.productCategoryRepo.findAllById(addProduct.getProductCategory()));
		product.setProductSubCategories(this.productSubCategoryRepo.findAllById(addProduct.getProductSubCategory()));
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
		try {
			if (product.getImage() != null && !product.getImage().isEmpty())
				product.setImage(this.storageService.uploadFileAsync(addProduct.getImage(), product.getUser().getId(),
						FolderConstants.PRODUCTS.getValue()).get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		product = this.productRepo.saveAndFlush(product);

		return new ProductBo(product);
	}

	@Override
	public ProductBo updateProduct(UpdateProduct updateProduct) {
		log.info("Inside update Product");
		Product product = this.productRepo.findById(updateProduct.getProductId()).get();
		if (product != null) {
			product.setProductName(updateProduct.getProductName());
			product.setProductCategories(this.productCategoryRepo.findAllById(updateProduct.getProductCategory()));
			product.setProductSubCategories(
					this.productSubCategoryRepo.findAllById(updateProduct.getProductSubCategory()));
			product.setUser(authenticationService.currentUser());
			try {
				product.setImage(this.storageService.uploadFileAsync(updateProduct.getImage(),
						product.getUser().getId(), FolderConstants.PRODUCTS.getValue()).get());
			} catch (InterruptedException | ExecutionException e) {

				e.printStackTrace();
			}
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
		throw new BadRequestException("Invalid Product Id");
	}

	@Override
	public ProductBo fetchProduct(String id) {
		log.info("Inside fetch product");
		Optional<Product> product = this.productRepo.findById(id);
		if (product.isPresent()) {
			return new ProductBo(product.get());
		}
		throw new BadRequestException("no product found with id= " + id);
	}

	@Override
	public ListResponseWithCount<ProductBo> fetchProductList(PaginationBo paginationBo) {
		User user = this.authenticationService.currentUser();
		Page<Product> allProducts =null;
		/*if(user.getRoles().size()==1&&user.getRoles().get(0).getRole().equals(RoleConstants.CUSTOMER.getValue())) {
			 allProducts=this.productRepo.findAll(PageRequest.of(paginationBo.getPageNumber(), pageSize));
		}*/
		if(paginationBo.getByUserflag()) {
			allProducts=this.productRepo.findByUser(PageRequest.of(paginationBo.getPageNumber(), pageSize),user);
		}else {
			allProducts=this.productRepo.findAll(PageRequest.of(paginationBo.getPageNumber(), pageSize));
		}
		List<ProductBo>bos = new ArrayList<ProductBo>();
		allProducts.forEach(product->bos.add(new ProductBo(product)));
		return new ListResponseWithCount<ProductBo>(bos,"",allProducts.getTotalElements(),paginationBo.getPageNumber(),allProducts.getTotalPages());
	}

}
