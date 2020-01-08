package com.electronicshop.service;

import java.util.List;

import com.electronicshop.entity.CartItem;
import com.electronicshop.entity.ShoppingCart;

public interface CartItemService {

	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	CartItem updateCartItem(CartItem cartItem);
}
