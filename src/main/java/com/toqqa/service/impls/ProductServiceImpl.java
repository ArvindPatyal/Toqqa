package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.constants.FileType;
import com.toqqa.constants.FolderConstants;
import com.toqqa.domain.Attachment;
import com.toqqa.domain.Product;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.UpdateProduct;
import com.toqqa.repository.AttachmentRepository;
import com.toqqa.repository.ProductCategoryRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.ProductSubCategoryRepository;
import com.toqqa.service.AttachmentService;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.ProductService;
import com.toqqa.service.StorageService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private AttachmentRepository attachmentRepository;

	@Override
	public ProductBo addProduct(AddProduct addProduct) {
		log.info("Inside Add Product");
		Product product = new Product();

		product.setProductName(addProduct.getProductName());
		product.setProductCategories(this.productCategoryRepo.findAllById(addProduct.getProductCategory()));
		product.setProductSubCategories(this.productSubCategoryRepo.findAllById(addProduct.getProductSubCategory()));
		product.setDescription(addProduct.getDescription());
		// product.setDetails(addProduct.getDetails());
		product.setUnitsInStock(addProduct.getUnitsInStock());
		product.setPricePerUnit(addProduct.getPricePerUnit());
		product.setDiscount(addProduct.getDiscount());
		product.setMaximumUitsInOneOrder(addProduct.getMaximumUnitsInOneOrder());
		product.setMinimumUnitsInOneOrder(addProduct.getMinimumUnitsInOneOrder());

		if(addProduct.getExpiryDate()!=null)
		product.setExpiryDate(new Date(addProduct.getExpiryDate()));

		product.setCountryOfOrigin(addProduct.getCountryOfOrigin());
		product.setManufacturerName(addProduct.getManufacturerName());
		product.setUser(authenticationService.currentUser());
		product.setIsDeleted(false);

		if(addProduct.getManufacturingDate()!=null)
		product.setManufacturingDate(new Date(addProduct.getManufacturingDate()));

		List<Attachment> attachments = new ArrayList<Attachment>();
		for (MultipartFile imageFile : addProduct.getImages()) {
			if (imageFile != null && !imageFile.isEmpty())
				try {
					String location = this.storageService
							.uploadFileAsync(imageFile, product.getUser().getId(), FolderConstants.PRODUCTS.getValue())
							.get();
					attachments.add(this.attachmentService.addAttachment(location, FileType.PRODUCT_IMAGE.getValue(),
							imageFile.getOriginalFilename(), imageFile.getContentType()));
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

		}

		try {
			if (addProduct.getBanner() != null && !addProduct.getBanner().isEmpty()) {
				product.setBanner(this.storageService.uploadFileAsync(addProduct.getBanner(), product.getUser().getId(),
						FolderConstants.LOGO.getValue()).get());
			}			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		product.setAttachments(attachments);
		product = this.productRepo.saveAndFlush(product);

		return new ProductBo(product,this.prepareAttachments(product.getAttachments()));
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

			product.setDescription(updateProduct.getDescription());
			// product.setDetails(updateProduct.getDetails());
			product.setUnitsInStock(updateProduct.getUnitsInStock());
			product.setPricePerUnit(updateProduct.getPricePerUnit());
			product.setDiscount(updateProduct.getDiscount());
			product.setMaximumUitsInOneOrder(updateProduct.getMaximumUnitsInOneOrder());
			product.setMinimumUnitsInOneOrder(updateProduct.getMinimumUnitsInOneOrder());

			if(updateProduct.getExpiryDate()!=null)
				product.setExpiryDate(new Date(updateProduct.getExpiryDate()));

			product.setCountryOfOrigin(updateProduct.getCountryOfOrigin());
			product.setManufacturerName(updateProduct.getManufacturerName());

			if(updateProduct.getManufacturingDate()!=null)
			product.setManufacturingDate(new Date(updateProduct.getManufacturingDate()));

			List<Attachment> attachments = new ArrayList<Attachment>();
			this.attachmentRepository.deleteAll(product.getAttachments());

			for (MultipartFile imageFile : updateProduct.getImages()) {
				if (imageFile != null && !imageFile.isEmpty())
					try {
						String location = this.storageService.uploadFileAsync(imageFile, product.getUser().getId(),
								FolderConstants.PRODUCTS.getValue()).get();
						attachments
								.add(this.attachmentService.addAttachment(location, FileType.PRODUCT_IMAGE.getValue(),
										imageFile.getOriginalFilename(), imageFile.getContentType()));
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}

			}
						
			try {
				if (updateProduct.getBanner() != null && !updateProduct.getBanner().isEmpty()) {
					product.setBanner(this.storageService.uploadFileAsync(updateProduct.getBanner(),
							product.getUser().getId(), FolderConstants.LOGO.getValue()).get());
				}				
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

			product.setAttachments(attachments);
			product = this.productRepo.saveAndFlush(product);
			return new ProductBo(product,this.prepareAttachments(product.getAttachments()));
		}
		throw new BadRequestException("Invalid Product Id");
	}

	private List<String> prepareAttachments(List<Attachment> attachments){
		List<String> atts = new ArrayList<>();
		final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		attachments.forEach(att->{
			atts.add(this.storageService.generatePresignedUrl(att.getLocation()));
		});
		return atts;
	}

	@Override
	public ProductBo fetchProduct(String id) {
		log.info("Inside fetch product");
		Optional<Product> product = this.productRepo.findById(id);
		if (product.isPresent()) {
			return new ProductBo(product.get(),this.prepareAttachments(product.get().getAttachments()));
		}
		throw new BadRequestException("no product found with id= " + id);
	}

	@Override
	public ListResponseWithCount<ProductBo> fetchProductList(PaginationBo paginationBo) {
		User user = this.authenticationService.currentUser();
		Page<Product> allProducts = null;

		if (this.authenticationService.isAdmin()) {
			allProducts = this.productRepo.findByIsDeleted(PageRequest.of(paginationBo.getPageNumber(), pageSize),false);
		} else {
			allProducts = this.productRepo.findByUserAndIsDeleted(PageRequest.of(paginationBo.getPageNumber(), pageSize), user,false);
		}
		List<ProductBo> bos = new ArrayList<ProductBo>();
		allProducts.forEach(product -> bos.add(new ProductBo(product,this.prepareAttachments(product.getAttachments()))));
		return new ListResponseWithCount<ProductBo>(bos, "", allProducts.getTotalElements(), paginationBo.getPageNumber(), allProducts.getTotalPages());

	}

	public void deleteProduct(String id) {

		Product prod = this.productRepo.findById(id).get();
		prod.setIsDeleted(true);
		this.productRepo.saveAndFlush(prod);

	}

}
