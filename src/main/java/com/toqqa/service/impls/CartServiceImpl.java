package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toqqa.bo.CartBo;
import com.toqqa.bo.CartItemBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Cart;
import com.toqqa.domain.CartItem;
import com.toqqa.domain.Product;
import com.toqqa.repository.CartItemRepository;
import com.toqqa.repository.CartRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.CartService;
import com.toqqa.service.CustomerService;
import com.toqqa.service.ProductService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
public class CartServiceImpl implements CartService {


    @Autowired
    private ProductService productService;
    @Autowired
    private Helper helper;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private CustomerService customerService;
    @Value("${pageSize}")
    private Integer pageSize;
  
	@Override
	public Response fetchCart(PaginationBo paginationBo) {
		log.info("Inside fetch cart");
	    CustomerProductRequest request = new CustomerProductRequest();
        request.setPageNumber(paginationBo.getPageNumber());

		ListResponseWithCount<ProductBo> productBoListResponseWithCount = this.customerService.productList(request);
		Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
		List<ProductBo> productBos = new ArrayList<>();
		List<CartItem> cartItem = cart.getCartItems();
		List<CartItemBo> itemBo = new ArrayList<>();
		CartBo cartBo = new CartBo(cart, itemBo);
		Double prc = 0.0;
		for (CartItem ci : cartItem) {
            ProductBo prdBo= new ProductBo(ci.getProduct(),this.helper.prepareProductAttachments(ci.getProduct().getAttachments()));
    		CartItemBo cartItemBo = new CartItemBo(ci,prdBo);
			itemBo.add(cartItemBo);
			Product product = ci.getProduct();
			Double price = product.getPricePerUnit() * ci.getQuantity()
					- ((product.getPricePerUnit() * ci.getQuantity()) / 100) * product.getDiscount();

			prc = prc + price;
    		cartBo.setSubTotal(prc);
    	}

		productBoListResponseWithCount.getData().forEach(productBo -> {
			if (isItemInCart(productBo, cart)) {
				productBos.add(productBo);
			}
		});

		return new Response(cartBo, " ");
	}

    public Response manageCart(CartItemPayload cartItemPayload) {
        log.info("Inside Service Add To cart");

        Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(authenticationService.currentUser());
            cart.setCartItems(this.persistCartItems(cartItemPayload, cart));
            this.cartRepo.saveAndFlush(cart);
        }
        else{
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
        CartItem cartItem = cartItemRepo.findByProductIdAndCart(cartItemPayload.getProductId(), cart);
        if (cartItemPayload.getQuantity() <= 0) {
            this.deleteCartItem(cartItemPayload.getProductId());
        } else {
            cartItem.setQuantity(cartItemPayload.getQuantity());
            cartItemRepo.saveAndFlush(cartItem);
        }
        return new Response(true, "Cart Updated Successfully");
    }

    @Override
    public Response deleteCartItem(String productId) {
        log.info("Inside Service delete cart item");
        Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
        Optional<Product> product = this.productRepo.findById(productId);
        if (!product.isPresent()) {
            throw new BadRequestException("Invalid Product Id");
        }
        cart.getCartItems().removeIf(cartItem -> cartItem.getProductId().equals(productId) && cartItem.getCart().equals(cart));
        cartItemRepo.deleteByCartIdAndProduct(cart.getId(), product);
        if (cart.getCartItems().size() <= 0) {
            cartRepo.delete(cart);
        }
        return new Response(true, "deleted Successfully");
    }

}
