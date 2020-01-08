package com.electronicshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.electronicshop.entity.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
