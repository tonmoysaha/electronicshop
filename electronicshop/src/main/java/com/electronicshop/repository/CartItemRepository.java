package com.electronicshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.electronicshop.entity.CartItem;
import com.electronicshop.entity.ShoppingCart;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

	List<CartItem> findByshoppingCart(ShoppingCart shoppingCart);
}
