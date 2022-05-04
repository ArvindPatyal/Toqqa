package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.domain.Wishlist;
import com.toqqa.payload.CustomerProductRequest;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.WishlistRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.CustomerService;
import com.toqqa.service.WishlistService;
import com.toqqa.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	@Override
	public ListResponseWithCount productList(CustomerProductRequest bo) {
		log.info("Inside productList");
		Page<Product> products = null;

		if (bo.getProductCategoryId() != null) {
			products = this.prRepository.findByProductCategories_IdInAndIsDeleted(
					PageRequest.of(bo.getPageNumber(), pageSize), bo.getProductCategoryId(), false);
		} else {
			products = productRepository.findByIsDeleted(PageRequest.of(bo.getPageNumber(), pageSize), false);
		}

		Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());

		List<ProductBo> productBos = new ArrayList<>();
		products.forEach(product -> {
			ProductBo productBo = new ProductBo(product);
			productBo.setIsInWishList(this.wishlistService.isWishListItem(productBo, wishlist));
			productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
			productBo.setImages(this.helper.prepareProductAttachments(product.getAttachments()));
			productBos.add(productBo);
		});
		return new ListResponseWithCount(productBos, "", products.getTotalElements(), bo.getPageNumber(),
				products.getTotalPages());
	}
}
