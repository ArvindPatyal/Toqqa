package com.toqqa.service.impls;

import com.toqqa.bo.WishlistBo;
import com.toqqa.bo.WishlistItemBo;
import com.toqqa.domain.Wishlist;
import com.toqqa.domain.WishlistItem;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.Response;
import com.toqqa.payload.WishlistItemPayload;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.WishlistItemRepository;
import com.toqqa.repository.WishlistRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.WishlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    WishlistItemRepository wishlistItemRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    ProductRepository productRepository;

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
                return new Response(true, "item removed from wishlist");
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
    public List<WishlistItemBo> fetchWishlistItems(Wishlist wishlist) {
        log.info("Inside Service show wishlist");

        List<WishlistItem> wishlistItems = wishlistItemRepository.findByWishlist(wishlist);
        List<WishlistItemBo> wishlistItemBo = new ArrayList<>();
        wishlistItems.forEach(item -> wishlistItemBo.add(new WishlistItemBo(item)));


        return wishlistItemBo;
    }

    @Override
    public WishlistBo fetchWishlist() {
        log.info("Inside  Service fetch wishlist");
        Wishlist wishlist = this.wishlistRepository.findByUser_Id(authenticationService.currentUser().getId());
        if (wishlist != null) {
            return new WishlistBo(wishlist, this.fetchWishlistItems(wishlist));
        }
        throw new BadRequestException("no wishlist such found ");
    }



    @Override
    public void deleteWishlistItem(String productId) {
        log.info("Inside Service delete wishlist");
        String wishlistId = wishlistRepository.findByUser_Id(authenticationService.currentUser().getId()).getId();
        wishlistItemRepository.deleteByProductIdAndWishlist_Id(productId, wishlistId);

    }
}
