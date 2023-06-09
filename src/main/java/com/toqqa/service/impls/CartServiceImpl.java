package com.toqqa.service.impls;

import com.toqqa.bo.CartBo;
import com.toqqa.bo.CartItemBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Cart;
import com.toqqa.domain.CartItem;
import com.toqqa.domain.Product;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.payload.CartItemPayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.repository.CartItemRepository;
import com.toqqa.repository.CartRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.repository.WishlistRepository;
import com.toqqa.service.*;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    @Lazy
    private WishlistService wishlistService;
    @Autowired
    private WishlistRepository wishlistRepository;

    @Override
    public Response fetchCart() {
        log.info("Invoked :: CartServiceImpl :: fetchCart()");
        Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
        List<ProductBo> productBos = new ArrayList<>();
        if (cart == null) {
            return new Response<>(null, "Cart not added");
        }
        ListResponse<ProductBo> bo = this.productService.productList();
        List<CartItem> cartItem = cart.getCartItems();
        List<CartItemBo> itemBo = new ArrayList<>();
        CartBo cartBo = new CartBo(cart, itemBo);
        Double prc = 0.0d;
        for (CartItem ci : cartItem) {
            ProductBo prdBo = this.productService.toProductBo(ci.getProduct());
            CartItemBo cartItemBo = new CartItemBo(ci, prdBo);
            itemBo.add(cartItemBo);
            Product product = ci.getProduct();
            Double price = product.getPricePerUnit() * ci.getQuantity()
                    - ((product.getPricePerUnit() * ci.getQuantity()) / 100) * product.getDiscount();

            prc = prc + price;
            cartBo.setSubTotal(this.helper.roundOff(prc));
        }

        bo.getData().forEach(productBo -> {
            if (isItemInCart(productBo, cart)) {
                productBos.add(productBo);
            }
        });

        return new Response(cartBo, " ");
    }

    public Response manageCart(CartItemPayload cartItemPayload) {
        log.info("Invoked :: CartServiceImpl :: manageCart()");
        User user = this.authenticationService.currentUser();
        Cart cart = this.cartRepo.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            boolean isValidProduct = productRepo.findAll().stream()
                    .anyMatch(product -> product.getId().equals(cartItemPayload.getProductId()));
            if (isValidProduct) {
                cart.setCartItems(this.persistCartItems(cartItemPayload, cart));
                this.cartRepo.saveAndFlush(cart);
            } else {
                throw new BadRequestException("Enter a valid product id");
            }
        } else {
            Boolean isExist = cart.getCartItems().stream()
                    .anyMatch(cartItem -> cartItem.getProduct().getId().equals(cartItemPayload.getProductId()));
            if (isExist) {
                CartItem cartItem = this.cartItemRepo.findByProductIdAndCart(cartItemPayload.getProductId(), cart);
                if (cartItem.getProduct().getUnitsInStock() >= cartItemPayload.getQuantity()) {
                    cartItem.setQuantity(cartItemPayload.getQuantity());
                    cartItemRepo.saveAndFlush(cartItem);
                } else {
                    throw new BadRequestException("You entered maximum quantity," + " " + "units left in stock :" + " " +
                            cartItem.getProduct().getUnitsInStock());
                }
            } else {
                boolean isValidProduct = productRepo.findAll().stream()
                        .anyMatch(product -> product.getId().equals(cartItemPayload.getProductId()));
                if (isValidProduct) {
                    cart.setCartItems(this.persistCartItems(cartItemPayload, cart));
                    this.cartRepo.saveAndFlush(cart);
                } else {
                    throw new BadRequestException("Enter a valid product Id");
                }
            }
        }

        return new Response(true, "Item added to Cart Successfully");
    }

    private List<CartItem> persistCartItems(CartItemPayload cartItemPayload, Cart cart) {
        log.info("Invoked :: CartServiceImpl :: persistCartItems()");
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
            return cart.getCartItems().stream()
                    .anyMatch(cartItem -> cartItem.getProduct().getId().equals(productBo.getId()));
        else
            return false;
    }

    @Override
    public Response updateCart(CartItemPayload cartItemPayload) {
        log.info("Invoked :: CartServiceImpl :: updateCart()");
        Cart cart = this.cartRepo.findByUser(this.authenticationService.currentUser());
        if (cart == null) {
            return new Response(true, "cart not found");
        } else {
            List<CartItem> cartItems = cart.getCartItems();
            Optional<CartItem> cartItemOptional = cartItems.stream()
                    .filter(item -> item.getProductId().equals(cartItemPayload.getProductId())).findFirst();
            if (cartItemOptional.isPresent()) {
                if (cartItemPayload.getQuantity() <= 0) {
                    return this.removeCartItem(cart, cartItemPayload.getProductId());
                } else {
                    CartItem cartItem = cartItemOptional.get();
                    cartItem.setQuantity(cartItemPayload.getQuantity());
                    cartItemRepo.saveAndFlush(cartItem);
                    return new Response("", "CartItem Updated Successfully");
                }
            } else {
                throw new BadRequestException("Enter a valid product Id which is already in cart");
            }
        }
    }

    private Response removeCartItem(Cart cart, String productId) {
        log.info("Invoked :: CartServiceImpl :: removeCartItem()");
        cart.getCartItems()
                .removeIf(cartItem -> cartItem.getProductId().equals(productId) && cartItem.getCart().equals(cart));
        cartItemRepo.deleteByCartAndProductId(cart, productId);
        if (cart.getCartItems().size() <= 0) {
            cartRepo.delete(cart);
        }
        return new Response(true, "cart Item removed Successfully");
    }

    @Override
    public Response deleteCartItem(String productId) {
        log.info("Invoked :: CartServiceImpl :: deleteCartItem()");
        Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());
        if (cart == null) {
            throw new ResourceNotFoundException("cart not found");
        }
        boolean isItemInCart = cart.getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getProductId().equals(productId));
        if (!isItemInCart) {
            throw new BadRequestException("Enter a valid product Id which is already in cart");
        } else {
            cart.getCartItems()
                    .removeIf(cartItem -> cartItem.getProductId().equals(productId) && cartItem.getCart().equals(cart));
            cartItemRepo.deleteByCartAndProductId(cart, productId);
            if (cart.getCartItems().size() <= 0) {
                cartRepo.delete(cart);
            }
            return new Response(true, " cart Item deleted Successfully");
        }
    }
}
