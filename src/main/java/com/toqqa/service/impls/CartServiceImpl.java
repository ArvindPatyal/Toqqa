package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toqqa.bo.CartBo;
import com.toqqa.bo.CartItemBo;
import com.toqqa.domain.Cart;
import com.toqqa.domain.CartItem;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.CartItemPayload;
import com.toqqa.payload.CartPayload;
import com.toqqa.payload.CartUpdate;
import com.toqqa.repository.CartItemRepository;
import com.toqqa.repository.CartRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.CartService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private CartItemRepository cartItemRepo;

	public CartBo addToCart(CartPayload cartPayload) {
		log.info("Inside Add To cart");

		Cart cart = this.cartRepo.findByUser(authenticationService.currentUser());

		if (cart == null) {
			cart = new Cart();
			cart.setCreatedDate(new Date());
			cart.setUser(authenticationService.currentUser());

		}
		cart.setModificationDate(new Date());
		cart = this.cartRepo.saveAndFlush(cart);
		cart.setCartItems(persistCartItems(cartPayload.getItems(), cart));

		return new CartBo(cart, this.fetchCartItems(cart));
	}

	private List<CartItem> persistCartItems(List<CartItemPayload> cartItems, Cart cart) {
		log.info("Inside persist cart items");
		List<CartItem> cartItem = new ArrayList<CartItem>();

		for (CartItemPayload cartitem : cartItems) {
			CartItem cItem = new CartItem();
			cItem.setAmount(cartitem.getAmount());
			cItem.setProduct(this.productRepo.findById(cartitem.getProductId()).get());
			cItem.setQuantity(cartitem.getQuantity());
			cItem.setCart(cart);
			cItem = this.cartItemRepo.saveAndFlush(cItem);
			cartItem.add(cItem);
		}
		return cartItem;
	}

	private List<CartItemBo> fetchCartItems(Cart cart) {
		log.info("Inside fetch cart items");
		List<CartItem> items = this.cartItemRepo.findByCart(cart);
		List<CartItemBo> itemBos = new ArrayList<CartItemBo>();
		items.forEach(item -> {

			itemBos.add(new CartItemBo(item));

		});
		return itemBos;
	}

	@Override
	public CartBo fetchCart(String id) {
		log.info("Inside fetch cart");
		Optional<Cart> cart = this.cartRepo.findById(id);
		if (cart.isPresent()) {
			return new CartBo(cart.get(), this.fetchCartItems(cart.get()));
		}
		throw new BadRequestException("no cart found with id= " + id);
	}

	@Override
	public CartBo updateCart(CartUpdate cartUpdate) {

		List<CartItem> cartItems = new ArrayList<CartItem>();

		Cart cart = this.cartRepo.findById(cartUpdate.getCartId()).get();

		CartItem cartItem = this.cartItemRepo.findByProduct_Id(cartUpdate.getProductId());

		cartItem.setQuantity(cartUpdate.getQuantity());

		cartItems.add(cartItem);

		this.cartItemRepo.saveAndFlush(cartItem);

		cart.setCartItems(cartItems);

		cart.setModificationDate(new Date());

		this.cartRepo.saveAndFlush(cart);

		List<CartItemBo> item = new ArrayList<>();

		cartItems.forEach(ite -> {

			item.add(new CartItemBo(ite));
		});

		return new CartBo(cart, item);
		// To do return list of cart items;
	}
}
