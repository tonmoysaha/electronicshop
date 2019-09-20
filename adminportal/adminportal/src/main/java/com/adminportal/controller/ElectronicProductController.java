package com.adminportal.controller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.entity.ElectronicProduct;
import com.adminportal.service.ElectronicProductService;

@Controller
@RequestMapping("/eproduct")
public class ElectronicProductController {
	
	@Autowired
	private ElectronicProductService ElectronicProductService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addElectronicProduct(Model model) {
		ElectronicProduct electronicProduct = new ElectronicProduct();
		model.addAttribute("electronicProduct",electronicProduct);
		
		return "addEProduct";
	}
	
	@RequestMapping(value = "/add" , method = RequestMethod.POST)
	public  String addElectronicProductPost(@ModelAttribute("electronicProduct") ElectronicProduct electronicProduct,
			HttpServletRequest request) {
		ElectronicProductService.save(electronicProduct);
		
		MultipartFile electronicProductImage = electronicProduct.getImage();
		
		try {
			byte[] bytes = electronicProductImage.getBytes();
			
			String name = electronicProduct.getId()+".png";
			
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/image/ElectronicProduct/"+name)));
			
			stream.write(bytes);
			stream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:electronicProductList";
	}
	
	
	@RequestMapping("/electronicProductList")
	public String electronicProductList() {
		
		return "electronicProductList";
	}
	
	
}
