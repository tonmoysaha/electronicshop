package com.adminportal;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.adminportal.entity.User;
import com.adminportal.entity.security.Role;
import com.adminportal.entity.security.UserRole;
import com.adminportal.service.UserService;
import com.adminportal.utility.SecurityUtility;

@SpringBootApplication
public class AdminportalApplication implements CommandLineRunner{
	
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(AdminportalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setFirstName("tonmoy saha");
		user.setLastName("opi");
		
		user.setUsername("admin");
		
		user.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
		
		user.setEmail("admin@gmail.com");
		
		Set<UserRole> userRoles = new HashSet<UserRole>();
		
		Role role = new Role();
		role.setRoleId(0);
		role.setName("ROLE_ADMIN");
		
		userRoles.add(new UserRole(user, role));
		
		userService.createUser(user, userRoles);
		
		
		
	}

}
