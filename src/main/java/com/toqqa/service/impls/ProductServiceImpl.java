package com.toqqa.service.impls;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.constants.FileType;
import com.toqqa.constants.FolderConstants;
import com.toqqa.constants.OrderBy;
import com.toqqa.domain.*;
import com.toqqa.dto.UpdateSequenceNumberDTO;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.InternalServerException;
import com.toqqa.payload.*;
import com.toqqa.repository.*;
import com.toqqa.service.*;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductCategoryRepository productCategoryRepo;
	@Autowired
	ProductSubCategoryRepository productSubCategoryRepo;
	@Autowired
	private ProductRepository productRepo;
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

	@Autowired
	private AdvertisementRepository advertisementRepo;

	@Autowired
	private Helper helper;

	@Autowired
	@Lazy
	private WishlistService wishlistService;

	@Autowired
	private WishlistRepository wishlistRepository;

	@Autowired
	private SmeRepository smeRepo;

	@Override
	public ProductBo addProduct(AddProduct addProduct) {
		log.info("Invoked :: ProductServiceImpl :: addProduct()");
		if (addProduct.getMaximumUnitsInOneOrder() != null && addProduct.getMinimumUnitsInOneOrder() != null) {
			if (addProduct.getMaximumUnitsInOneOrder() < addProduct.getMinimumUnitsInOneOrder()) {
				throw new BadRequestException("Max. value greater then min. value");
			}
		}
		Product product = new Product();
		product.setProductName(addProduct.getProductName());
		product.setProductCategories(this.productCategoryRepo.findAllById(addProduct.getProductCategory()));
		product.setProductSubCategories(this.productSubCategoryRepo.findAllById(addProduct.getProductSubCategory()));
		product.setDescription(addProduct.getDescription());
		// product.setDetails(addProduct.getDetails());
		product.setUnitsInStock(addProduct.getUnitsInStock());
		product.setPricePerUnit(addProduct.getPricePerUnit());
		product.setDiscount(addProduct.getDiscount());
		product.setMaximumUnitsInOneOrder(addProduct.getMaximumUnitsInOneOrder());
		product.setMinimumUnitsInOneOrder(addProduct.getMinimumUnitsInOneOrder());
		if (addProduct.getExpiryDate() != null)
			product.setExpiryDate(new Date(addProduct.getExpiryDate()));

		product.setCountryOfOrigin(addProduct.getCountryOfOrigin());
		product.setManufacturerName(addProduct.getManufacturerName());
		product.setUser(authenticationService.currentUser());
		product.setIsDeleted(false);
		product.setDeliveredInSpecifiedRadius(addProduct.getDeliveredInSpecifiedRadius());
		product.setDelieveredOutsideSpecifiedRadius(addProduct.getDelieveredOutsideSpecifiedRadius());
		if (addProduct.getManufacturingDate() != null)
			product.setManufacturingDate(new Date(addProduct.getManufacturingDate()));

		product = this.productRepo.saveAndFlush(product);
		List<Attachment> attachments = new ArrayList<>();
		for (MultipartFile imageFile : addProduct.getImages()) {
			if (imageFile != null && !imageFile.isEmpty())
				try {
					String location = this.storageService
							.uploadFileAsync(imageFile, product.getUser().getId(), FolderConstants.PRODUCTS.getValue())
							.get();
					attachments.add(this.attachmentService.addAttachment(location, FileType.PRODUCT_IMAGE.getValue(),
							imageFile.getOriginalFilename(), imageFile.getContentType(), product));

					if (addProduct.getBanner().getOriginalFilename().equals(imageFile.getOriginalFilename())) {
						product.setBanner(location);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
		}

		product.setAttachments(attachments);
		product.setAttachments(attachments);
		product = this.productRepo.saveAndFlush(product);

		ProductBo bo = new ProductBo(product, this.helper.prepareProductAttachments(product.getAttachments()));
		bo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
		return bo;
	}

	/*private String prepareResource(String location) {
		if (this.helper.notNullAndBlank(location)) {
			return this.storageService.generatePresignedUrl(location);
		}
		return "";
	}*/

	@Override
	public ProductBo updateProduct(UpdateProduct updateProduct) {
		log.info("Invoked :: ProductServiceImpl :: updateProduct()");

		if (updateProduct.getMaximumUnitsInOneOrder() != null && updateProduct.getMinimumUnitsInOneOrder() != null) {
			if (updateProduct.getMaximumUnitsInOneOrder() < updateProduct.getMinimumUnitsInOneOrder()) {
				throw new BadRequestException("Max. value greater then min. value");
			}
		}

		Product product = this.productRepo.findById(updateProduct.getProductId()).get();
		if (product != null) {
			product.setProductName(updateProduct.getProductName());
			product.setProductCategories(this.productCategoryRepo.findAllById(updateProduct.getProductCategory()));
			product.setProductSubCategories(
					this.productSubCategoryRepo.findAllById(updateProduct.getProductSubCategory()));
//          product.setUser(authenticationService.currentUser());

			product.setDescription(updateProduct.getDescription());
			// product.setDetails(updateProduct.getDetails());
			product.setUnitsInStock(updateProduct.getUnitsInStock());
			product.setPricePerUnit(updateProduct.getPricePerUnit());
			product.setDiscount(updateProduct.getDiscount());
			product.setMaximumUnitsInOneOrder(updateProduct.getMaximumUnitsInOneOrder());
			product.setMinimumUnitsInOneOrder(updateProduct.getMinimumUnitsInOneOrder());

			if (updateProduct.getExpiryDate() != null)
				product.setExpiryDate(new Date(updateProduct.getExpiryDate()));

			product.setCountryOfOrigin(updateProduct.getCountryOfOrigin());
			product.setManufacturerName(updateProduct.getManufacturerName());

			if (updateProduct.getManufacturingDate() != null)
				product.setManufacturingDate(new Date(updateProduct.getManufacturingDate()));

			if (this.helper.notNullAndBlank(updateProduct.getBanner())) {
				Attachment attachment = this.attachmentRepository.findById(updateProduct.getBanner()).get();
				product.setBanner(attachment.getLocation());
			}
			product.setDeliveredInSpecifiedRadius(updateProduct.getDeliveredInSpecifiedRadius());
			product.setDelieveredOutsideSpecifiedRadius(updateProduct.getDelieveredOutsideSpecifiedRadius());
			product = this.productRepo.saveAndFlush(product);

			ProductBo bo = new ProductBo(product, this.helper.prepareProductAttachments(product.getAttachments()));
			bo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
			return bo;
		}
		throw new BadRequestException("Invalid Product Id");
	}

	@Override
	public ProductBo fetchProduct(String id) {
		log.info("Invoked :: ProductServiceImpl :: fetchProduct()");
		Optional<Product> product = this.productRepo.findById(id);
		if (product.isPresent()) {
			return this.toProductBo(product.get());
		}
		throw new BadRequestException("no product found with id= " + id);
	}

	@Override
	public ListResponseWithCount<ProductBo> fetchProductList(ListProductRequest paginationBo) {
		log.info("Invoked :: ProductServiceImpl :: fetchProductList()");
		User user = this.authenticationService.currentUser();
		Page<Product> allProducts = null;

		if (this.authenticationService.isAdmin()) {
			allProducts = this.productRepo.findByIsDeleted(PageRequest.of(paginationBo.getPageNumber(), pageSize),
					paginationBo.getIsInActive());
		} else {
			allProducts = this.productRepo.findByUserAndIsDeleted(
					PageRequest.of(paginationBo.getPageNumber(), pageSize), user, paginationBo.getIsInActive());
		}
		List<ProductBo> bos = new ArrayList<>();
		allProducts.forEach(product -> {
			bos.add(this.toProductBo(product));
		});
		return new ListResponseWithCount<ProductBo>(bos, "", (allProducts.getTotalElements()),
				(paginationBo.getPageNumber()), (allProducts.getTotalPages()));

	}

	public void deleteProduct(String id) {
		log.info("Invoked :: ProductServiceImpl :: deleteProduct()");
		Product prod = this.productRepo.findById(id).get();
		prod.setIsDeleted(true);
		Advertisement adv = this.advertisementRepo.findByProduct_Id(id);
		if (adv != null)
			adv.setIsActive(false);
		this.productRepo.saveAndFlush(prod);

	}

	@Override
	public Boolean deleteAttachment(String id) {
		log.info("Invoked :: ProductServiceImpl :: deleteAttachment()");
		if (this.attachmentRepository.findById(id).isPresent()) {
			this.attachmentRepository.deleteById(id);
			return true;
		}
		throw new BadRequestException("invalid attachment id: " + id);
	}

	@Override
	public ListResponse<FileBo> updateProductImage(FileUpload file) {
		log.info("Invoked :: ProductServiceImpl :: updateProductImage()");
		Product prod = this.productRepo.findById(file.getId()).get();
		List<FileBo> presignedUrls = new ArrayList<>();
		if (prod != null) {
			file.getImages().forEach(image -> {

				try {
					String location = this.storageService
							.uploadFileAsync(image, prod.getUser().getId(), FolderConstants.PRODUCTS.getValue()).get();
					Attachment attachment = this.attachmentService.addAttachment(location,
							FileType.PRODUCT_IMAGE.getValue(), image.getOriginalFilename(), image.getContentType(),
							prod);
					presignedUrls.add(new FileBo(attachment.getId(), this.helper.prepareResource(location)));
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			});
			return new ListResponse<>(presignedUrls, "");
		}
		throw new BadRequestException("Invalid Product Id");

	}

	@Override
	public ProductBo updateProductStatus(ToggleStatus toggleStatus) {
		log.info("Invoked :: ProductServiceImpl :: updateProductStatus()");
		if (!this.authenticationService.isSME()) {
			throw new AccessDeniedException("user is not an sme");
		}
		Optional<Product> prd = this.productRepo.findById(toggleStatus.getId());
		if (prd.isPresent()) {
			Product prds = prd.get();
			prds.setIsDeleted(toggleStatus.getStatus());
			prds = this.productRepo.saveAndFlush(prds);
			ProductBo bo = new ProductBo(prds, this.helper.prepareProductAttachments(prds.getAttachments()));
			bo.setBanner(this.helper.prepareAttachmentResource(prds.getBanner()));
			return new ProductBo(prds);
		}

		throw new BadRequestException("invalid product id " + toggleStatus.getId());
	}

	@Override
	public ListResponse<ProductBo> productList() {
		log.info("Invoked :: ProductServiceImpl :: productList()");
		List<Product> products = null;

		products = this.productRepo.findAll();
		Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
		List<ProductBo> productBos = new ArrayList<>();
		products.forEach(product -> {
			productBos.add(this.toProductBo(product));
		});
		return new ListResponse<ProductBo>(productBos, "");
	}

	@Override
	public ListResponseWithCount<ProductBo> searchProducts(PaginationBo bo) {
		log.info("Invoked :: ProductServiceImpl :: searchProducts()");
		Page<Product> page = null;

		Sort sort = this.sortBy(bo);
		if (this.helper.notNullAndBlank(bo.getSearchText()) && this.helper.notNullAndBlank(bo.getCategoryId())) {
			page = this.productRepo.findByProductCategories_IdAndProductNameContainsOrDescriptionContainsAndIsDeleted(
					PageRequest.of(bo.getPageNumber(), pageSize, sort), bo.getCategoryId(),
					bo.getSearchText().trim(), bo.getSearchText().trim(),false);

		} else if (this.helper.notNullAndBlank(bo.getSearchText())) {
			page = this.productRepo.findByIsDeletedAndProductNameContainsOrDescriptionContains(
					PageRequest.of(bo.getPageNumber(), pageSize, sort), false, bo.getSearchText().trim(),
					bo.getSearchText().trim());
		} else if (this.helper.notNullAndBlank(bo.getCategoryId())) {
			page = this.productRepo.findByProductCategories_IdAndIsDeleted(
					PageRequest.of(bo.getPageNumber(), pageSize, sort), bo.getCategoryId(), false);
		} else {
			page = this.productRepo.findByIsDeleted(PageRequest.of(bo.getPageNumber(), pageSize, sort), false);
		}
		List<ProductBo> productBos = new ArrayList<>();
		Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
		page.get().forEach(product -> {
			productBos.add(this.toProductBo(product));
		});
		return new ListResponseWithCount<ProductBo>(productBos, "", page.getTotalElements(), bo.getPageNumber(),
				page.getTotalPages());
	}

	private Sort sortBy(PaginationBo bo) {
		if (this.helper.notNullAndBlank(bo.getSortKey()) && helper.notNullAndBlank(bo.getSortOrder())) {
			if (bo.getSortOrder().equals(OrderBy.DESC.name())) {

				return Sort.by(bo.getSortKey()).descending();
			} else {
				return Sort.by(bo.getSortKey()).ascending();
			}
		}
		return Sort.by("createdAt").descending();
	}

	@Override
	public ProductBo toProductBo(Product product) {
		Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
		Sme sme = this.smeRepo.findByUserId(product.getUser().getId());
		ProductBo productBo = new ProductBo(product);
		if (sme != null) {
			SmeBo smeBo = new SmeBo(sme);
			smeBo.setBusinessLogo(this.helper.prepareResource(smeBo.getBusinessLogo()));
			productBo.setSellerDetails(smeBo);
		}
		productBo.setIsInWishList(this.wishlistService.isWishListItem(productBo, wishlist));
		productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
		productBo.setImages(this.helper.prepareProductAttachments(product.getAttachments()));

		return productBo;
	}

	@Override
	public Boolean updateSequenceNumber(UpdateSequenceNumberDTO dto) {
		log.info("Invoked :: ProductServiceImpl :: updateSequenceNumber()");
		Optional<Product> product = this.productRepo.findById(dto.getProductId());
		if(product.isPresent()){
			product.get().setSequenceNumber(dto.getSequenceNumber());
			this.productRepo.saveAndFlush(product.get());
			return true;
		}
		throw new InternalServerException("unable to update sequence!!");
	}
}