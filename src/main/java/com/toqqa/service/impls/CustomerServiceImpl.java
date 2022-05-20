package com.toqqa.service.impls;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.domain.Wishlist;
import com.toqqa.payload.CustomerProductRequest;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ProductRequestFilter;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.WishlistRepository;
import com.toqqa.service.*;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
	private WishlistService wishlistService;
	@Autowired
	private WishlistRepository wishlistRepository;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private ProductRepository prRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private SmeService smeService;

	@Override
	public ListResponseWithCount productList(CustomerProductRequest bo) {
		log.info("Inside productList");
		Page<Product> products = null;

		if (helper.notNullAndHavingData(bo.getProductCategoryIds())) {
			products = this.prRepository.findByProductCategories_IdInAndIsDeleted(
					PageRequest.of(bo.getPageNumber(), pageSize), bo.getProductCategoryIds(), false);
		} else {
			products = productRepository.findByIsDeleted(PageRequest.of(bo.getPageNumber(), pageSize), false);
		}
		Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());

		List<ProductBo> productBos = new ArrayList<>();
		products.forEach(product -> {
			productBos.add(this.productService.toProductBo(product));
		});
		return new ListResponseWithCount(productBos, "", products.getTotalElements(), bo.getPageNumber(),
				products.getTotalPages());
	}

	@Override
	public ListResponseWithCount smeProductList(ProductRequestFilter productRequestFilter) {
		log.info("Inside Service smeProductList");
		return this.smeService.fetchProducts(productRequestFilter);
	}
}
