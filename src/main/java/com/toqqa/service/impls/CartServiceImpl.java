package com.toqqa.service.impls;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Cart;
import com.toqqa.domain.CartItem;
import com.toqqa.domain.Product;
import com.toqqa.payload.*;
import com.toqqa.repository.CartItemRepository;
import com.toqqa.repository.CartRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.CartService;
import com.toqqa.service.ProductService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
public class CartServiceImpl implements CartService {

    @Autowired
    ProductService productService;
    @Autowired
    Helper helper;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CartItemRepository cartItemRepo;

    public Response manageCart(CartItemPayload cartItemPayload) {
        log.info("Inside Service Add To cart");

        Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(authenticationService.currentUser());
            cart.setCartItems(this.persistCartItems(cartItemPayload, cart));
            this.cartRepo.saveAndFlush(cart);
        }
        if (cart != null) {
            Boolean isExist = cart.getCartItems().stream().anyMatch(cartItem -> cartItem.getProduct().getId().equals(cartItemPayload.getProductId()));
            if (isExist) {
                CartItem cartItem = this.cartItemRepo.findByProductIdAndCart(cartItemPayload.getProductId(), cart);
                cartItem.setQuantity(cartItemPayload.getQuantity());
                cartItemRepo.saveAndFlush(cartItem);
            } else {
                cart.setCartItems(this.persistCartItems(cartItemPayload, cart));
                this.cartRepo.saveAndFlush(cart);
            }
        }

        return new Response(true, "Item added to Cart Successfully");
    }

    private List<CartItem> persistCartItems(CartItemPayload cartItemPayload, Cart cart) {
        log.info("Inside persist cart items");
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductId(cartItemPayload.getProductId());
        cartItem.setProduct(this.productRepo.getById(cartItemPayload.getProductId()));
        cartItem.setQuantity(cartItemPayload.getQuantity());
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        return cartItems;
    }

    @Override
    public ListResponse fetchCart(PaginationBo paginationBo) {
        log.info("Inside fetch cart");
        ListProductRequest listProductRequest = new ListProductRequest(false);
        listProductRequest.setPageNumber(paginationBo.getPageNumber());
        ListResponseWithCount<ProductBo> productBoListResponseWithCount = this.productService.fetchProductList(listProductRequest);
        Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
        List<ProductBo> productBos = new ArrayList<>();
        productBoListResponseWithCount.getData().forEach(productBo -> {
            if (isItemInCart(productBo, cart)) {
                productBos.add(productBo);
            }
        });
        return new ListResponse(productBos, "fetching cart items");
    }


    private Boolean isItemInCart(ProductBo productBo, Cart cart) {
        if (cart != null && this.helper.notNullAndHavingData(cart.getCartItems()))
            return cart.getCartItems().stream().anyMatch(cartItem -> cartItem.getProduct().getId().equals(productBo.getId()));
        else return false;
    }


    @Override
    public Response updateCart(CartItemPayload cartItemPayload) {

        Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
        if (cart == null) {
            return new Response(true, "cart not found");
        }
        CartItem cartItem = cartItemRepo.findByProduct_Id(cartItemPayload.getProductId());
        if (cartItemPayload.getQuantity() <= 0) {
            this.deleteCartItem(cartItemPayload.getProductId());
        } else {
            cartItem.setQuantity(cartItemPayload.getQuantity());
            cartItemRepo.saveAndFlush(cartItem);
        }
        return new Response(true, "Cart Updated Sucessfully");
    }

    @Override
    public Response deleteCartItem(String productId) {
        log.info("Inside Service delete cart item");
        Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
        Product product = this.productRepo.getById(productId);
        cart.getCartItems().removeIf(cartItem -> cartItem.getProductId().equals(productId) && cartItem.getCart().equals(cart));
        cartItemRepo.deleteByCartIdAndProduct(cart.getId(), product);
        if (cart.getCartItems().size() <= 0) {
            cartRepo.delete(cart);
        }
        return new Response(true, "deleted Sucessfully");
    }

}
