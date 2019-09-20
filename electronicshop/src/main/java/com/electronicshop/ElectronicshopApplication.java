package com.electronicshop;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.electronicshop.entity.User;
import com.electronicshop.entity.security.Role;
import com.electronicshop.entity.security.UserRole;
import com.electronicshop.service.UserService;
import com.electronicshop.utility.SecurityUtility;

@SpringBootApplication
public class ElectronicshopApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicshopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		
		user.setFirstName("ma");
		user.setLastName("love");
		
		user.setUsername("ma love");
		
		user.setPassword(SecurityUtility.passwordEncoder().encode("ma"));
		
		user.setEmail("malove@gmail.com");
		
		Set<UserRole> userRoles = new HashSet<UserRole>();
		
		Role role = new Role();
		
		role.setRoleId(2);
		role.setName("ROLE_USER");
		userRoles.add(new UserRole(user, role));
		
		userService.createUser(user, userRoles);
		
	}
	
	
}
