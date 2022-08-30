package com.toqqa.service.impls;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.payload.CustomerProductRequest;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ProductRequestFilter;
import com.toqqa.repository.ProductCategoryRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.CustomerService;
import com.toqqa.service.ProductService;
import com.toqqa.service.SmeService;
import com.toqqa.util.Constants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private final ProductRepository productRepository;
    private final Helper helper;
    private final ProductService productService;
    private final SmeService smeService;
    private final ProductCategoryRepository productCategoryRepository;
    private final HashSet<String> PRODUCT_CATEGORIES;
    @Value("${pageSize}")
    private Integer pageSize;
    @Value("${bulk.product.minimum.quantity}")
    private Integer bulkProductQuantity;

    @Autowired
    public CustomerServiceImpl(ProductRepository productRepository, Helper helper,
                               ProductService productService, SmeService smeService,
                               ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.helper = helper;
        this.productService = productService;
        this.smeService = smeService;
        this.productCategoryRepository = productCategoryRepository;
        PRODUCT_CATEGORIES = (HashSet<String>) productCategoryRepository.findAll().stream().map(productCategory -> productCategory.getId()).collect(Collectors.toSet());
    }

    @Override
    public ListResponseWithCount<ProductBo> productList(CustomerProductRequest customerProductRequest) {
        log.info("Invoked :: CustomerServiceImpl :: productList()");
        customerProductRequest.setSortOrder(Constants.SORT_ORDERS.contains(customerProductRequest.getSortOrder()) ? customerProductRequest.getSortOrder() : "DESC");
        customerProductRequest.setSortKey(Constants.PRODUCT_SORT_KEYS.contains(customerProductRequest.getSortKey()) ? customerProductRequest.getSortKey() : "discountedPrice");
        customerProductRequest.setProductCategoryIds(this.helper.notNullAndHavingData(customerProductRequest.getProductCategoryIds()) && PRODUCT_CATEGORIES.containsAll(customerProductRequest.getProductCategoryIds()) ? customerProductRequest.getProductCategoryIds() : (List<String>) PRODUCT_CATEGORIES);
        Page<Product> products;
        if (this.helper.notNullAndBlank(customerProductRequest.getSearchText())) {
            products = this.productRepository.findByProductCategoriesIdInAndProductNameContainsAndIsDeletedOrProductCategoriesIdInAndDescriptionContainsAndIsDeleted(
                    PageRequest.of(customerProductRequest.getPageNumber(), pageSize, Sort.by(Sort.Direction.fromString(customerProductRequest.getSortOrder()), customerProductRequest.getSortKey())),
                    customerProductRequest.getProductCategoryIds(), customerProductRequest.getSearchText().trim(), false, customerProductRequest.getProductCategoryIds()
                    , customerProductRequest.getSearchText().trim(), false);
        } else if (customerProductRequest.getShowBulkProducts()) {
            products = this.productRepository
                    .findByProductCategories_IdInAndIsDeletedAndMinimumUnitsInOneOrderGreaterThanEqual(
                            customerProductRequest.getProductCategoryIds(), false, PageRequest.of(customerProductRequest.getPageNumber(), pageSize, Sort.by(Sort.Direction.fromString(customerProductRequest.getSortOrder()), customerProductRequest.getSortKey())), 2);
        } else {
            products = this.productRepository.findByProductCategories_IdInAndIsDeleted(
                    PageRequest.of(customerProductRequest.getPageNumber(), pageSize, Sort.by(Sort.Direction.fromString(customerProductRequest.getSortOrder()), customerProductRequest.getSortKey())), customerProductRequest.getProductCategoryIds(), false);
        }
        return new ListResponseWithCount<ProductBo>(products.stream().map(this.productService::toProductBo).collect(Collectors.toList()),
                "", products.getTotalElements(), customerProductRequest.getPageNumber(),
                products.getTotalPages());
    }

    @Override
    public ListResponseWithCount<?> smeProductList(ProductRequestFilter productRequestFilter) {
        log.info("Invoked :: CustomerServiceImpl :: smeProductList()");
        return this.smeService.fetchProducts(productRequestFilter);
    }
}
