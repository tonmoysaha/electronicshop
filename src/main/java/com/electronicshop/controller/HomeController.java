package com.electronicshop.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.electronicshop.entity.User;
import com.electronicshop.entity.security.PasswordResetToken;
import com.electronicshop.entity.security.Role;
import com.electronicshop.entity.security.UserRole;
import com.electronicshop.service.UserService;
import com.electronicshop.service.impl.UserSecurityService;
import com.electronicshop.utility.MailConstructor;
import com.electronicshop.utility.SecurityUtility;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;

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
	
	@RequestMapping(value = "/newuser",method = RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username, Model model) throws Exception {
		
		      model.addAttribute("ClassActiveNewAccount", true);
		      model.addAttribute("email",userEmail);
		      model.addAttribute("username",userEmail);
		      
		      if (userService.findByUsername(username) != null) {
				model.addAttribute("usernameExists", true);
				return "myAccount";
			}
		      if (userService.findByEmail(userEmail) != null) {
					model.addAttribute("email", true);
					return "myAccount";
				}
			
		      User user = new User();
		      user.setEmail(userEmail);
		      user.setUsername(username);
		      
		      String password = SecurityUtility.randomPassword();
		      String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		      user.setPassword(encryptedPassword);
		      
		      Role role = new Role();
		      role.setRoleId(1);
		      role.setName("ROLE_USER");
		      Set<UserRole> userRoles = new HashSet<UserRole>();
		      userRoles.add(new UserRole(user, role));
		      
		      userService.createUser(user,userRoles);
		      
		      String token = UUID.randomUUID().toString();
		      userService.createPasswordResetToken(user, token);
		      
		      String appUrl = "http://"+ request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		      
		     SimpleMailMessage email = mailConstructor.constructResetToken(appUrl, request.getLocale(), token, user, encryptedPassword);
		     
		     mailSender.send(email);
		     
		     model.addAttribute("emailSent", true);
		     
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
		theModel.addAttribute("ClassActiveEdit", true);
		return "myProfile";
	}
}
