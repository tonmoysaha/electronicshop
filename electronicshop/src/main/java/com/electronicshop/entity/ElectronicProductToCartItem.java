package com.electronicshop.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ElectronicProductToCartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "electronicProduct_id")
	private ElectronicProduct electronicProduct;
	
	@ManyToOne
	@JoinColumn(name = "cart_item_id")
	private CartItem cartItem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ElectronicProduct getElectronicProduct() {
		return electronicProduct;
	}

	public void setElectronicProduct(ElectronicProduct electronicProduct) {
		this.electronicProduct = electronicProduct;
	}

	public CartItem getCartItem() {
		return cartItem;
	}

	public void setCartItem(CartItem cartItem) {
		this.cartItem = cartItem;
	}
	
	
}
