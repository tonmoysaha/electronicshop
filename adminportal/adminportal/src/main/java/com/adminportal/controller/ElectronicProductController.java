package com.adminportal.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adminportal.entity.ElectronicProduct;

@Controller
@RequestMapping("/eproduct")
public class ElectronicProductController {

	@RequestMapping("/add")
	public String addBook(Model model) {
		ElectronicProduct electronicProduct = new ElectronicProduct();
		model.addAttribute("electronicProduct",electronicProduct);
		
		return "addEProduct";
	}
}
