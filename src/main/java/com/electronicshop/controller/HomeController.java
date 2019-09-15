package com.electronicshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.electronicshop.entity.User;
import com.electronicshop.entity.security.PasswordResetToken;
import com.electronicshop.service.UserService;
import com.electronicshop.service.impl.UserSecurityService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/myAccount")
	public String myAccount() {
		return "myAccount";
	}

	@RequestMapping("/login")
	public String login(Model theModel) {
		theModel.addAttribute("ClassActiveLogin", true);
		return "myAccount";
	}

	@RequestMapping("/forgetpassword")
	public String forgetPassword(Model theModel) {

		theModel.addAttribute("ClassActiveForgetPassword", true);
		return "myAccount";
	}

	@RequestMapping("/newuser")
	public String newUser(Local local, @RequestParam("token") String token, Model theModel) {
		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
		if (passwordResetToken == null) {
			String massage = "invalid Token";
			theModel.addAttribute("massage", massage);
			return "redirect:/badRequest";
		}
		User user = passwordResetToken.getUser();

		String username = user.getUsername();
		UserDetails userDetails = userSecurityService.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		theModel.addAttribute("ClassActiveedit", true);
		return "myProfile";
	}
}
