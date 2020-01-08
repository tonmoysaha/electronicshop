package com.electronicshop.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicshop.entity.CartItem;
import com.electronicshop.entity.ShoppingCart;
import com.electronicshop.repository.ShoppingCartRepository;
import com.electronicshop.service.CartItemService;
import com.electronicshop.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	@Override
	public ShoppingCart updateShoppingcart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		BigDecimal cartTotall = new BigDecimal(0);
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

		for (CartItem cartItem : cartItemList) {
			if (cartItem.getElectronicProduct().getInStockNumber() > 0) {
				cartItemService.updateCartItem(cartItem);
				cartTotall = cartTotall.add(cartItem.getSubTotal());
			}
		}

		shoppingCart.setGrandTotal(cartTotall);

		return shoppingCartRepository.save(shoppingCart);

	}

}
