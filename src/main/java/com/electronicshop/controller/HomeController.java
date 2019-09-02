package com.electronicshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@RequestMapping("/")
	public String index() {
		return "index";
		
	}
	
	@RequestMapping("/myaccount")
	public String myAccount() {
		return "myAccount";
		
	}
	
	@RequestMapping("/login")
	public String login(Model thModel) {
		thModel.addAttribute("ClassActiveLogin", true);
		return "myAccount";
		
	}

}
