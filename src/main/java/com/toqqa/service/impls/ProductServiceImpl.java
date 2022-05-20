package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.constants.FileType;
import com.toqqa.constants.FolderConstants;
import com.toqqa.constants.OrderBy;
import com.toqqa.domain.Advertisement;
import com.toqqa.domain.Attachment;
import com.toqqa.domain.Product;
import com.toqqa.domain.Sme;
import com.toqqa.domain.User;
import com.toqqa.domain.Wishlist;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.FileUpload;
import com.toqqa.payload.ListProductRequest;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ProductRequestFilter;
import com.toqqa.payload.ToggleStatus;
import com.toqqa.payload.UpdateProduct;
import com.toqqa.repository.AdvertisementRepository;
import com.toqqa.repository.AttachmentRepository;
import com.toqqa.repository.ProductCategoryRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.ProductSubCategoryRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.repository.WishlistRepository;
import com.toqqa.service.AttachmentService;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.ProductService;
import com.toqqa.service.StorageService;
import com.toqqa.service.WishlistService;
import com.toqqa.util.Helper;

import lombok.extern.slf4j.Slf4j;

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
		log.info("Inside Add Product");
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

		/*
		 * try { if (addProduct.getBanner() != null &&
		 * !addProduct.getBanner().isEmpty()) {
		 * product.setBanner(this.storageService.uploadFileAsync(addProduct.getBanner(),
		 * product.getUser().getId(), FolderConstants.BANNER.getValue()).get()); } }
		 * catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
		 */

		product.setAttachments(attachments);
		product = this.productRepo.saveAndFlush(product);

		ProductBo bo = new ProductBo(product, this.helper.prepareProductAttachments(product.getAttachments()));
		bo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
		return bo;
	}

	private String prepareResource(String location) {
		if (this.helper.notNullAndBlank(location)) {
			return this.storageService.generatePresignedUrl(location);
		}
		return "";
	}

	@Override
	public ProductBo updateProduct(UpdateProduct updateProduct) {
		log.info("inside update Product");

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
		log.info("Inside fetch product");
		Optional<Product> product = this.productRepo.findById(id);
		if (product.isPresent()) {
			return this.toProductBo(product.get());
//			ProductBo bo = new ProductBo(product.get(),
//					this.helper.prepareProductAttachments(product.get().getAttachments()));
//			bo.setBanner(this.helper.prepareAttachmentResource(product.get().getBanner()));
//			bo.setIsInWishList(this.wishlistService.isWishListItem(bo,
//					this.wishlistRepository.findByUser_Id(this.authenticationService.currentUser().getId())));
//			return bo;
		}
		throw new BadRequestException("no product found with id= " + id);
	}

	@Override
	public ListResponseWithCount<ProductBo> fetchProductList(ListProductRequest paginationBo) {
		log.info("inside fetch products");
		User user = this.authenticationService.currentUser();
		Page<Product> allProducts = null;

		if (this.authenticationService.isAdmin()) {
			allProducts = this.productRepo.findByIsDeleted(PageRequest.of(paginationBo.getPageNumber(), pageSize),
					paginationBo.getIsInActive());
		} else {
			allProducts = this.productRepo.findByUserAndIsDeleted(
					PageRequest.of(paginationBo.getPageNumber(), pageSize), user, paginationBo.getIsInActive());
		}
		List<ProductBo> bos = new ArrayList<ProductBo>();
		allProducts.forEach(product -> {
//			ProductBo bo = new ProductBo(product, this.helper.prepareProductAttachments(product.getAttachments()));
//			bo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
			bos.add(this.toProductBo(product));
		});
		return new ListResponseWithCount<ProductBo>(bos, "", (allProducts.getTotalElements()),
				(paginationBo.getPageNumber()), (allProducts.getTotalPages()));

	}

	@Override
	public ListResponseWithCount smeProductListFilter(ProductRequestFilter ProductRequestFilter) {
		log.info("Inside get product list");
		if (!this.helper.notNullAndHavingData(ProductRequestFilter.getProductCategoryIds())) {
			return this.fetchProductList(ProductRequestFilter);
		} else {
			return this.filterProductListSme(ProductRequestFilter);
		}

	}

//    private ListResponseWithCount fetchProductsWithoutFilter(SmeProductRequestFilter smeProductRequestFilter, User user) {
//        log.info("Inside fetch products without filter");
//        Page<Product> products = this.productRepo.findByUserAndIsDeleted(PageRequest.of(smeProductRequestFilter.getPageNumber(), pageSize), user, smeProductRequestFilter.getIsInActive());
//        List<ProductBo> productBos = new ArrayList<>();
//        products.forEach(product -> {
//            ProductBo productBo = new ProductBo(product, this.helper.prepareProductAttachments(product.getAttachments()));
//            productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
//            productBos.add(productBo);
//        });
//
//        return new ListResponseWithCount(productBos, "products fetched", products.getNumberOfElements(), (smeProductRequestFilter.getPageNumber()), (products.getTotalPages()));
//    }

	private ListResponseWithCount filterProductListSme(ProductRequestFilter ProductRequestFilter) {
		log.info("Indside fetch product with filter");
		User user = new User();
		Page<Product> products = this.productRepo.findByProductCategories_IdInAndIsDeletedAndUser_Id(
				PageRequest.of(ProductRequestFilter.getPageNumber(), pageSize),
				ProductRequestFilter.getProductCategoryIds(), ProductRequestFilter.getIsInActive(), (user));
		List<ProductBo> productBos = new ArrayList<>();
		products.forEach(product -> {
//			ProductBo productBo = new ProductBo(product,
//					this.helper.prepareProductAttachments(product.getAttachments()));
//			productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
			productBos.add(this.toProductBo(product));
		});
		return new ListResponseWithCount(productBos, "products fetched", products.getTotalElements(),
				(ProductRequestFilter.getPageNumber()), (products.getTotalPages()));
	}

	public void deleteProduct(String id) {
		log.info("inside delete product");
		Product prod = this.productRepo.findById(id).get();
		prod.setIsDeleted(true);
		Advertisement adv = this.advertisementRepo.findByProduct_Id(id);
		if (adv != null)
			adv.setIsActive(false);
		this.productRepo.saveAndFlush(prod);

	}

	@Override
	public Boolean deleteAttachment(String id) {
		log.info("inside delete attachment");
		if (this.attachmentRepository.findById(id).isPresent()) {
			this.attachmentRepository.deleteById(id);
			return true;
		}
		throw new BadRequestException("invalid attachment id: " + id);
	}

	@Override
	public ListResponse<FileBo> updateProductImage(FileUpload file) {
		log.info("inside upload product image");
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
		log.info("Inside update Product status");
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
	public ListResponse productList() {
		log.info("Inside productList");
		List<Product> products = null;

		products = this.productRepo.findAll();
		Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());

		List<ProductBo> productBos = new ArrayList<>();
		products.forEach(product -> {
//			ProductBo productBo = new ProductBo(product);
//			productBo.setIsInWishList(this.wishlistService.isWishListItem(productBo, wishlist));
//			productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
//			productBo.setImages(this.helper.prepareProductAttachments(product.getAttachments()));
			productBos.add(this.toProductBo(product));
		});
		return new ListResponse(productBos, null);
	}

	@Override
	public ListResponseWithCount<ProductBo> searchProducts(PaginationBo bo) {
		Page<Product> page = null;
		Sort sort = this.sortBy(bo);
		String param = " ";
		param = bo.getSearchText().trim();
		page = this.productRepo.fetchProducts(PageRequest.of(bo.getPageNumber(), pageSize, sort), param);
		List<ProductBo> bos = new ArrayList();
		Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());

		page.get().forEach(product -> {
//			ProductBo productBo = new ProductBo(product);
//			productBo.setIsInWishList(this.wishlistService.isWishListItem(productBo, wishlist));
//			productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
//			productBo.setImages(this.helper.prepareProductAttachments(product.getAttachments()));
			bos.add(this.toProductBo(product));
		});
		return new ListResponseWithCount(bos, "", page.getTotalElements(), bo.getPageNumber(), page.getTotalPages());
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
			productBo.setSellerDetails(smeBo);
		}
		productBo.setIsInWishList(this.wishlistService.isWishListItem(productBo, wishlist));
		productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
		productBo.setImages(this.helper.prepareProductAttachments(product.getAttachments()));

		return productBo;
	}
}