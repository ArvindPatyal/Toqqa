package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.payload.CustomerProductRequest;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ProductRequestFilter;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.CustomerService;
import com.toqqa.service.ProductService;
import com.toqqa.service.SmeService;
import com.toqqa.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private Helper helper;

	@Value("${pageSize}")
	private Integer pageSize;

	@Autowired
	private ProductService productService;

	@Autowired
	private SmeService smeService;

	@Override
	public ListResponseWithCount<ProductBo> productList(CustomerProductRequest bo) {
		log.info("Inside productList");
		Page<Product> products = null;
		Sort sort = Sort.by("createdAt").descending();
		if (helper.notNullAndHavingData(bo.getProductCategoryIds()) && bo.getShowBulkProducts()) {
			products = this.productRepository
					.findByProductCategories_IdInAndIsDeletedAndMinimumUnitsInOneOrderGreaterThanEqual(
							bo.getProductCategoryIds(), false, PageRequest.of(bo.getPageNumber(), pageSize,sort), 2);
		} else if (bo.getShowBulkProducts()) {
			products = this.productRepository.findByIsDeletedAndMinimumUnitsInOneOrderGreaterThanEqual(false,
					PageRequest.of(bo.getPageNumber(), pageSize,sort), 2);
		} else if (helper.notNullAndHavingData(bo.getProductCategoryIds())) {
			products = this.productRepository.findByProductCategories_IdInAndIsDeleted(
					PageRequest.of(bo.getPageNumber(), pageSize,sort), bo.getProductCategoryIds(), false);
		} else {
			products = productRepository.findByIsDeleted(PageRequest.of(bo.getPageNumber(), pageSize,sort), false);
		}

		List<ProductBo> productBos = new ArrayList<>();
		products.forEach(product -> {
			productBos.add(this.productService.toProductBo(product));
		});
		return new ListResponseWithCount<ProductBo>(productBos, "", products.getTotalElements(), bo.getPageNumber(),
				products.getTotalPages());
	}

	@Override
	public ListResponseWithCount<?> smeProductList(ProductRequestFilter productRequestFilter) {
		log.info("Inside Service smeProductList");
		return this.smeService.fetchProducts(productRequestFilter);
	}
}
