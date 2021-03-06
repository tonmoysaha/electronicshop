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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.electronicshop.entity.ElectronicProduct;
import com.electronicshop.entity.User;
import com.electronicshop.entity.UserBilling;
import com.electronicshop.entity.UserPayment;
import com.electronicshop.entity.UserShipping;
import com.electronicshop.entity.security.PasswordResetToken;
import com.electronicshop.entity.security.Role;
import com.electronicshop.entity.security.UserRole;
import com.electronicshop.service.ElectronicProductService;
import com.electronicshop.service.UserPaymentService;
import com.electronicshop.service.UserService;
import com.electronicshop.service.UserShippingService;
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

	@Autowired
	private UserPaymentService userPaymentService;

	@Autowired
	private UserShippingService userShippingService;

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

	@RequestMapping(value = "/newuser", method = RequestMethod.GET)
	public String newUser(Locale local, @RequestParam("token") String token, Model theModel) {

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

		if (passwordResetToken == null) {
			String massage = "invalid Token";
			theModel.addAttribute("massage", massage);
			return "redirect:/badRequest";
		}
		User user = passwordResetToken.getUser();
		String email = user.getEmail();

		UserDetails userDetails = userSecurityService.loadUserByUsername(email);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
		theModel.addAttribute("user", user);

		theModel.addAttribute("classActiveEdit", true);
		return "myProfile";
	}

	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword,
			@ModelAttribute("currentPassword") String currentPassword ,Model model) throws Exception {
		
		User currentUser = userService.findById(user.getId());

		if (currentUser == null) {
			throw new Exception("User not found");
		}

		/* check email already exists */
		if (userService.findByEmail(user.getEmail()) != null) {
			if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("emailExists", true);
				return "myProfile";
			}
		}

		/* check username already exists */
		if (userService.findByUsername(user.getUsername()) != null) {
			if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernameExists", true);
				return "myProfile";
			}
		}

//		update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if (passwordEncoder.matches(currentPassword, dbPassword)) {
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);

				return "myProfile";
			}
		}

		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setUsername(user.getUsername());
		currentUser.setEmail(user.getEmail());

		userService.save(currentUser);

		model.addAttribute("updateSuccess", true);
		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);

		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "myProfile";
	}



	@RequestMapping("/myProfile")
	public String myProfilePage(Model model, Principal principal) {
		String username = principal.getName();

		User user = userService.findByUsername(username);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());

		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);

		List<String> stateList = BdContants.listofStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("listofCreditCards", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("classActiveEdit", true);

		return "myProfile";

	}

	// credit card

	@RequestMapping("/listofCreditCard")
	public String listofCreditCards(Model model, Principal principal, HttpServletRequest request) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());

		model.addAttribute("listofCreditCards", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("classActiveBilling", true);

		return "myProfile";

	}

	@RequestMapping(value = "/addNewCreditCard", method = RequestMethod.GET)
	public String addNewCreditCardPage(Model model, Principal principal) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("classActiveBilling", true);
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("listOfShippingAddreses", true);

		UserPayment userPayment = new UserPayment();
		UserBilling userBilling = new UserBilling();

		model.addAttribute("userPayment", userPayment);
		model.addAttribute("userBilling", userBilling);

		List<String> stateList = BdContants.listofStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());

		return "myProfile";

	}

	@RequestMapping(value = "/addNewCreditCard", method = RequestMethod.POST)
	public String addNewCreditCard(@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling, HttpServletRequest request, Principal principal,
			Model model) {
		User user = userService.findByUsername(principal.getName());

		userService.updateUserBilling(userBilling, userPayment, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("listofCreditCards", true);

		return "myProfile";

	}

	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(Model model, @ModelAttribute("id") Long id, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		UserPayment userPayment = userPaymentService.findById(id);
		UserBilling userBilling = userPayment.getUserBilling();

		if (user.getId() != userPayment.getUser().getId()) {
			return "badRequest";

		} else {

			model.addAttribute("user", user);
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());

			List<String> stateList = BdContants.listofStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			model.addAttribute("classActiveBilling", true);
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("listOfShippingAddreses", true);
			return "myProfile";

		}

	}

	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(Model model, @ModelAttribute("id") Long creditId, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		UserPayment userPayment = userPaymentService.findById(creditId);

		if (user.getId() != userPayment.getUser().getId()) {
			return "badRequest";

		} else {

			userPaymentService.removeById(creditId);

			model.addAttribute("user", user);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddreses", true);
			model.addAttribute("listofCreditCards", true);

			return "myProfile";
		}

	}

	@RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
	public String setDefaultPayment(Model model, @ModelAttribute("defaultUserPaymentId") Long defaultUserPaymentId,
			Principal principal) {
		User user = userService.findByUsername(principal.getName());

		userService.setUserDefaultPayment(defaultUserPaymentId, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("listofCreditCards", true);

		return "myProfile";

	}

	// shipping Address

	@RequestMapping("/listOfShippingAddreses")
	public String listOfShippingAddreses(Model model, Principal principal, HttpServletRequest request) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());

		model.addAttribute("listofCreditCards", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("classActiveShipping", true);

		return "myProfile";

	}

	@RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.GET)
	public String addNewShippingAddresePage(Model model, Principal principal) {

		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("classActiveShipping", true);
		model.addAttribute("addNewShippingAddress", true);

		UserShipping userShipping = new UserShipping();

		model.addAttribute("userShipping", userShipping);

		List<String> stateList = BdContants.listofStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("userShippingList", user.getUserShippingList());

		return "myProfile";

	}

	@RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.POST)
	public String addNewShippingAddrese(@ModelAttribute("userShipping") UserShipping userShipping, Model model,
			Principal principal) {

		User user = userService.findByUsername(principal.getName());

		userService.updateUserShipping(userShipping, user);

		model.addAttribute("user", user);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddreses", true);
		model.addAttribute("userShippingList", user.getUserShippingList());

		return "myProfile";

	}

	@RequestMapping("/updateUserShipping")
	public String updateNewShippingAddrese(@ModelAttribute("id") Long userShippingId, Model model,
			Principal principal) {

		User user = userService.findByUsername(principal.getName());

		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (user.getId() != userShipping.getUser().getId()) {
			return "badRequest";
		} else {
			userService.updateUserShipping(userShipping, user);

			List<String> stateList = BdContants.listofStatesCode;
			Collections.sort(stateList);

			model.addAttribute("stateList", stateList);
			model.addAttribute("user", user);
			model.addAttribute("userShipping", userShipping);
			model.addAttribute("userShippingList", user.getUserShippingList());

			model.addAttribute("classActiveShipping", true);

			model.addAttribute("addNewShippingAddress", true);

			return "myProfile";

		}

	}

	@RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
	public String setDefaultShippingAddress(
			@ModelAttribute("defaultUserShippingAddressId") Long defaultUserShippingAddressId, Model model,
			Principal principal) {

		User user = userService.findByUsername(principal.getName());
		userShippingService.setDefaultUserShipping(defaultUserShippingAddressId, user);

		model.addAttribute("user", user);

		model.addAttribute("userShippingList", user.getUserShippingList());

		model.addAttribute("classActiveShipping", true);

		model.addAttribute("listOfShippingAddreses", true);
		return "myProfile";
	}

	@RequestMapping("/removeUserShipping")
	public String removeUserShippingAddress(@ModelAttribute("id") Long userShippingId, Model model,
			Principal principal) {

		User user = userService.findByUsername(principal.getName());

		userShippingService.deleteById(userShippingId);

		model.addAttribute("user", user);

		model.addAttribute("userShippingList", user.getUserShippingList());

		model.addAttribute("classActiveShipping", true);

		model.addAttribute("listOfShippingAddreses", true);
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
