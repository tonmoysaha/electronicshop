package com.electronicshop.service.impl;

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

}
