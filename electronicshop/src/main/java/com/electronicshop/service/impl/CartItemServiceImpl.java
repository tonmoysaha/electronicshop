package com.electronicshop.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicshop.entity.CartItem;
import com.electronicshop.entity.ShoppingCart;
import com.electronicshop.repository.CartItemRepository;
import com.electronicshop.service.CartItemService;
@Service
public class CartItemServiceImpl implements CartItemService {
	
	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		return cartItemRepository.findByshoppingCart(shoppingCart);
	}

	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		// TODO Agenerateduto- method stub
		BigDecimal bigDecimal = new BigDecimal(cartItem.getElectronicProduct().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));

		bigDecimal= bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		cartItem.setSubTotal(bigDecimal);
		
		cartItemRepository.save(cartItem);
		
		return cartItem;
		
	}

}
