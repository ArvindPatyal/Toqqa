package com.toqqa.service.impls;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.domain.Wishlist;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.WishlistRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.CustomerService;
import com.toqqa.service.WishlistService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public ListResponseWithCount productList(PaginationBo bo) {
        Page<Product> products = productRepository.findByIsDeleted(PageRequest.of(bo.getPageNumber(),pageSize),false);
        Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
        List<ProductBo> productBos = new ArrayList<>();
        products.forEach(product -> {
            ProductBo productBo = new ProductBo(product);
            productBo.setIsInWishList(this.wishlistService.isWishListItem(productBo,wishlist));
            productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
            productBo.setImages(this.helper.prepareProductAttachments(product.getAttachments()));
            productBos.add(productBo);
        });
        return new ListResponseWithCount(productBos, "",products.getTotalElements(),bo.getPageNumber(),products.getTotalPages());
    }
}


