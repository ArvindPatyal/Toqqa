package com.toqqa.service.impls;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.domain.Wishlist;
import com.toqqa.domain.WishlistItem;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.payload.WishlistItemPayload;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.WishlistItemRepository;
import com.toqqa.repository.WishlistRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.CustomerService;
import com.toqqa.service.ProductService;
import com.toqqa.service.WishlistService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private Helper helper;
    @Autowired
    @Lazy
    private CustomerService customerService;

    @Override

    public Response toggleWishlist(WishlistItemPayload wishlistItemPayload) {
        log.info("Inside Service create wishlist");

        Optional<Product> optionalProduct = this.productRepository.findById(wishlistItemPayload.getProductId());
        if (optionalProduct.isPresent()) {
            Wishlist wishlist = this.wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());

            if (wishlist == null) {
                wishlist = new Wishlist();
                wishlist.setUser(authenticationService.currentUser());
            } else {
                Boolean isExists = wishlist.getWishlistItems().stream().anyMatch(wishlistItem -> wishlistItem.getProductId().equals(wishlistItemPayload.getProductId()));
                if (isExists) {
                    this.deleteWishlistItem(wishlistItemPayload.getProductId());
                    return new Response(true, "item removed successfully");
                }

            }
            wishlist = this.wishlistRepository.saveAndFlush(wishlist);
            Product product = optionalProduct.get();
            wishlist.setWishlistItems(persistWishlistItems(wishlistItemPayload, wishlist,product));
            return new Response(true, "item added to wishlist");
        } else {
            throw new BadRequestException("product not found with id " + wishlistItemPayload.getProductId());
        }
    }


    private List<WishlistItem> persistWishlistItems(WishlistItemPayload wishlistItemsPayload, Wishlist wishlist,Product product) {
        log.info("Inside persist wishlist items");

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setProductId(wishlistItemsPayload.getProductId());
        wishlistItem.setProduct(productRepository.getById(wishlistItemsPayload.getProductId()));
        wishlistItem.setWishlist(wishlist);
        wishlistItem = wishlistItemRepository.saveAndFlush(wishlistItem);

        return Arrays.asList(wishlistItem);
    }

    @Override
    public ListResponse fetchWishlist() {
        log.info("inside Service fetch wishlist");
        Wishlist wishlist = this.wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
        if (wishlist == null) {
            return new ListResponse(null, "Wishlist Not Found");
        }

        ListResponse<ProductBo> list = this.productService.productList();

        List<ProductBo> wishlistProducts = new ArrayList<>();
        list.getData().forEach(productBo -> {
            if (this.isWishListItem(productBo, wishlist)) {
                productBo.setIsInWishList(true);
                wishlistProducts.add(productBo);
            }
        });
        return new ListResponse(wishlistProducts, "Wishlist fectched successfully ");
    }

    @Override
    public Boolean isWishListItem(ProductBo productBo, Wishlist wishlist) {
        if (wishlist != null && helper.notNullAndHavingData(wishlist.getWishlistItems()))
            return wishlist.getWishlistItems()
                    .stream().anyMatch(wishlistItem -> wishlistItem.getProductId().equals(productBo.getId()));
        else
            return false;
    }

    @Override
    public void deleteWishlistItem(String productId) {
        log.info("Inside Service delete wishlist");
        Wishlist wishlist = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
        if (wishlist != null && this.helper.notNullAndHavingData(wishlist.getWishlistItems())) {
            wishlistItemRepository.deleteByProductIdAndWishlist(productId, wishlist);
            wishlist.getWishlistItems().removeIf(wishlistItem -> wishlistItem.getProductId().equals(productId) && wishlistItem.getWishlist().equals(wishlist));
            if (wishlist.getWishlistItems().size() <= 0) {
                wishlistRepository.delete(wishlist);
            }
        }
    }
}
