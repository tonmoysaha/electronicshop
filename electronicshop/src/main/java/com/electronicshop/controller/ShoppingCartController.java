package com.electronicshop.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.electronicshop.entity.CartItem;
import com.electronicshop.entity.ShoppingCart;
import com.electronicshop.entity.User;
import com.electronicshop.service.CartItemService;
import com.electronicshop.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingcart(shoppingCart);
		
		model.addAttribute("shoppingCart");
		model.addAttribute("cartItemList", cartItemList);
		
		return null;
		
	}

}
