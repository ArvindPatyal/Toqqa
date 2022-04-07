package com.toqqa.service.impls;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.constants.FileType;
import com.toqqa.constants.FolderConstants;
import com.toqqa.domain.Attachment;
import com.toqqa.domain.Product;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.*;
import com.toqqa.repository.*;
import com.toqqa.service.AttachmentService;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.ProductService;
import com.toqqa.service.StorageService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private UserRepository userRepository;

    @Autowired
    private Helper helper;

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
        product.setMaximumUnitsInOneOrder(addProduct.getMaximumUnitsInOneOrder());
        product.setMinimumUnitsInOneOrder(addProduct.getMinimumUnitsInOneOrder());

        if (addProduct.getExpiryDate() != null)
            product.setExpiryDate(new Date(addProduct.getExpiryDate()));

        product.setCountryOfOrigin(addProduct.getCountryOfOrigin());
        product.setManufacturerName(addProduct.getManufacturerName());
        product.setUser(authenticationService.currentUser());
        product.setIsDeleted(false);

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
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

        }
        product.setAttachments(attachments);

        try {
            if (addProduct.getBanner() != null && !addProduct.getBanner().isEmpty()) {
                product.setBanner(this.storageService.uploadFileAsync(addProduct.getBanner(), product.getUser().getId(),
                        FolderConstants.BANNER.getValue()).get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        product.setAttachments(attachments);
        product = this.productRepo.saveAndFlush(product);

        ProductBo bo = new ProductBo(product, this.helper.prepareProductAttachments(product.getAttachments()));
        bo.setBanner(this.helper.prepareResource(bo.getBanner()));
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

            product = this.productRepo.saveAndFlush(product);

           /* List<Attachment> attachments = new ArrayList<>();

            if(this.helper.notNullAndHavingData(updateProduct.getImages()))
            for (MultipartFile imageFile : updateProduct.getImages()) {
                if (imageFile != null && !imageFile.isEmpty())
                    try {
                        String location = this.storageService.uploadFileAsync(imageFile, product.getUser().getId(),
                                FolderConstants.PRODUCTS.getValue()).get();
                        attachments.add(this.attachmentService.addAttachment(location, FileType.PRODUCT_IMAGE.getValue(),
                                imageFile.getOriginalFilename(), imageFile.getContentType(), product));
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
            }

            product.setAttachments(attachments);*/
            ProductBo bo = new ProductBo(product, this.helper.prepareProductAttachments(product.getAttachments()));
            bo.setBanner(this.helper.prepareResource(bo.getBanner()));
            return bo;
        }
        throw new BadRequestException("Invalid Product Id");
    }

    @Override
    public ProductBo fetchProduct(String id) {
        log.info("Inside fetch product");
        Optional<Product> product = this.productRepo.findById(id);
        if (product.isPresent()) {
            ProductBo bo = new ProductBo(product.get(), this.helper.prepareProductAttachments(product.get().getAttachments()));
            bo.setBanner(this.prepareResource(bo.getBanner()));
            return bo;
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
            allProducts = this.productRepo
                    .findByUserAndIsDeleted(PageRequest.of(paginationBo.getPageNumber(), pageSize), user, paginationBo.getIsInActive());
        }
        List<ProductBo> bos = new ArrayList<ProductBo>();
        allProducts.forEach(product -> {
            ProductBo bo = new ProductBo(product, this.helper.prepareProductAttachments(product.getAttachments()));
            bo.setBanner(this.prepareResource(bo.getBanner()));
            bos.add(bo);
        });
        return new ListResponseWithCount<ProductBo>(bos, "", allProducts.getTotalElements(),
                paginationBo.getPageNumber(), allProducts.getTotalPages());

    }

    public void deleteProduct(String id) {
        log.info("inside delete product");
        Product prod = this.productRepo.findById(id).get();
        prod.setIsDeleted(true);
        this.productRepo.saveAndFlush(prod);

    }

    @Override
    public Boolean deleteAttachment(String id) {
        log.info("inside delete attachment");
        if(this.attachmentRepository.findById(id).isPresent()) {
            this.attachmentRepository.deleteById(id);
            return true;
        }
        throw new BadRequestException("invalid attachment id: " +id);
    }

    @Override
    public ListResponse<FileBo> updateProductImage(FileUpload file) {
		log.info("inside upload product image");
        Product prod = this.productRepo.findById(file.getId()).get();
        List<FileBo>presignedUrls= new ArrayList<>();
        if (prod != null) {
            file.getImages().forEach(image->{

                    try {
                        String location = this.storageService
                                .uploadFileAsync(image, prod.getUser().getId(), FolderConstants.PRODUCTS.getValue()).get();
                        Attachment attachment = this.attachmentService.addAttachment(location,
                                FileType.PRODUCT_IMAGE.getValue(), image.getOriginalFilename(), image.getContentType(),
                                prod);
                        presignedUrls.add(new FileBo(attachment.getId(),this.helper.prepareResource(location)));
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
            return new ProductBo(prds);

        }

        throw new BadRequestException("invalid product id " + toggleStatus.getId());
    }

}
