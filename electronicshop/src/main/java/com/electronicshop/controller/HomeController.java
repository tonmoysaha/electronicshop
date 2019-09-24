package com.electronicshop.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.electronicshop.entity.ElectronicProduct;
import com.electronicshop.entity.User;
import com.electronicshop.entity.UserPayment;
import com.electronicshop.entity.UserShipping;
import com.electronicshop.entity.security.PasswordResetToken;
import com.electronicshop.entity.security.Role;
import com.electronicshop.entity.security.UserRole;
import com.electronicshop.service.ElectronicProductService;
import com.electronicshop.service.UserService;
import com.electronicshop.service.impl.UserSecurityService;
import com.electronicshop.utility.MailConstructor;
import com.electronicshop.utility.SecurityUtility;
import com.electronicshop.utility.BdContants;

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

	@Autowired
	private ElectronicProductService ElectronicProductService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/login")
	public String login(Model theModel) {
		theModel.addAttribute("ClassActiveLogin", true);
		return "myAccount";
	}

	@RequestMapping("/forgetpassword")
	public String forgetPassword(HttpServletRequest request, @ModelAttribute("email") String email,
			@ModelAttribute("username") String username, Model model) {

		model.addAttribute("ClassActiveForgetPassword", true);

		User user = userService.findByEmail(email);

		if (user == null) {
			model.addAttribute("emailNotExists", true);
			return "myAccount";
		}

		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		userService.save(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetToken(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage newEmail = mailConstructor.constructResetToken(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(newEmail);

		model.addAttribute("forgetPasswordEmailSent", true);

		return "myAccount";
	}
	

	@RequestMapping(value = "/newuser", method = RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username, Model model) throws Exception {

		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", userEmail);

		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			return "myAccount";
		}
		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
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

		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetToken(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetToken(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(email);

		model.addAttribute("emailSent", true);

		return "myAccount";

	}
	
	
	

	@RequestMapping(value = "/newuser" , method = RequestMethod.GET)
	public String newUser(Locale local, @RequestParam("token") String token, Model theModel) {
		
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
		theModel.addAttribute("user", user);

		theModel.addAttribute("classActiveEdit", true);
		return "myProfile";
	}
	
	
	
	@RequestMapping("/myProfile")
	public String myProfilePage(Model model , Principal principal) {
		String username = principal.getName();
		
		User user = userService.findByUsername(username);
		
		model.addAttribute("user",user);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		
		UserShipping userShipping =new UserShipping();
		model.addAttribute("userShipping",userShipping);
		
		List<String> stateList = BdContants.listofStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList",stateList);
		
		
		
		model.addAttribute("listofCreditCards", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("classActiveEdit", true);
	
		return "myProfile";
		
	}

	
	@RequestMapping("/listofCreditCard")
	public String listofCreditCards(Model model, Principal principal, HttpServletRequest request) {
		
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		
		model.addAttribute("listofCreditCards", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("classActiveBilling" , true);
		
		
		return "myProfile";
		
	}
	
	@RequestMapping("/listOfShippingAddreses")
	public String listOfShippingAddreses(Model model, Principal principal, HttpServletRequest request) {
		
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		
		model.addAttribute("listofCreditCards", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("classActiveBilling" , true);
		
		
		return "myProfile";
		
	}
	
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal){
		
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		
		model.addAttribute("classActiveBilling" , true);
		model.addAttribute("addNewCreditCard" , true);
		model.addAttribute("listOfShippingAddreses", true);
		
		
		UserPayment userPayment = new  UserPayment();
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userPayment",userPayment);
		model.addAttribute("userShipping",userShipping);
		
		
		List<String> stateList = BdContants.listofStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList",stateList);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		
		return "myProfile";
		
	}
	
	
	@RequestMapping("/addNewShippingAddrese")
	public String addNewShippingAddrese(Model model, Principal principal){
		
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		
		model.addAttribute("classActiveShipping" , true);
		model.addAttribute("addNewShippingAddrese" , true);
		
		
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userShipping",userShipping);
		
		
		List<String> stateList = BdContants.listofStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList",stateList);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		
		return "myProfile";
		
	}
	

	@RequestMapping("/electronicProductShelf")
	public String electronicProductShelf(Model model) {
		List<ElectronicProduct> electronicProductList = ElectronicProductService.findAll();
		model.addAttribute("electronicProductList", electronicProductList);
		return "electronicProductShelf";

	}

	
	
	
	@RequestMapping("/electronicProductDetails")
	public String electronicProductDetails(@PathParam("id") Long id, Model model, Principal principal) {

		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}

		ElectronicProduct electronicProduct = ElectronicProductService.findById(id);

		model.addAttribute("electronicProduct", electronicProduct);

		List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);

		return "electronicProductDetails";

	}

}
