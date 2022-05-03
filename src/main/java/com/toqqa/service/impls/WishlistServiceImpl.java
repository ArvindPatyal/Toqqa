package com.toqqa.service.impls;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.bo.WishlistItemBo;
import com.toqqa.domain.Wishlist;
import com.toqqa.domain.WishlistItem;
import com.toqqa.payload.*;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.WishlistItemRepository;
import com.toqqa.repository.WishlistRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.ProductService;
import com.toqqa.service.WishlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private WishlistItemRepository wishlistItemRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Override

    public Response toggleWishlist(WishlistItemPayload wishlistItemPayload) {
        log.info("Inside Service create wishlist");
        Wishlist wishlist = this.wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());

        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setUser(authenticationService.currentUser());
        } else {
            Boolean isExists = wishlist.getWishlistItems().stream().anyMatch(wishlistItem -> wishlistItem.getProductId().equals(wishlistItemPayload.getProductId()));
            if (isExists) {
                this.deleteWishlistItem(wishlistItemPayload.getProductId());
                return new Response(true, "item added to wishlist");
            }

        }
        wishlist = this.wishlistRepository.saveAndFlush(wishlist);
        wishlist.setWishlistItems(persistWishlistItems(wishlistItemPayload, wishlist));
        return new Response(true, "item added to wishlist");
    }


    private List<WishlistItem> persistWishlistItems(WishlistItemPayload wishlistItemsPayload, Wishlist wishlist) {
        log.info("Inside persist wishlist items");

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setProductId(wishlistItemsPayload.getProductId());
        wishlistItem.setProduct(productRepository.getById(wishlistItemsPayload.getProductId()));
        wishlistItem.setWishlist(wishlist);
        wishlistItem = wishlistItemRepository.saveAndFlush(wishlistItem);

        return Arrays.asList(wishlistItem);
    }

    @Override
    public ListResponse fetchWishlist(PaginationBo bo) {
        log.info("inside Service fetch wishlist");
        ListProductRequest request = new ListProductRequest(false);
        request.setPageNumber(bo.getPageNumber());
        ListResponseWithCount<ProductBo> list = this.productService.fetchProductList(request);
        Wishlist wishlist = this.wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
        System.out.println(list.getData().size());

        List<ProductBo> wishlistProducts =  list.getData().stream().filter(productBo -> {
            return productBo.getId().equals(wishlist.getWishlistItems()
                    .stream().map(wishlistItem -> wishlistItem.getProductId()).findFirst().get());
        }).collect(Collectors.toList());
        return new ListResponse(wishlistProducts, "");
    }

    @Override
    public Boolean isWishListItem(List<ProductBo> productBos, Wishlist wishlist){
        return productBos.stream().anyMatch(productBo -> {
            return productBo.getId().equals(wishlist.getWishlistItems()
                    .stream().map(wishlistItem -> wishlistItem.getProductId()).findFirst().get());
        });
    }

    @Override
    public void deleteWishlistItem(String productId) {
        log.info("Inside Service delete wishlist");
        Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
        wishlistItemRepository.deleteByProductIdAndWishlist_Id(productId, wishlist.getId());
        if (wishlist.getWishlistItems().size() <= 0) {
            wishlistRepository.delete(wishlist);
        }
    }
}
