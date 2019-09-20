package com.adminportal.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.User;
import com.adminportal.entity.security.UserRole;
import com.adminportal.repository.UserRepository;
import com.adminportal.repository.RoleRepository;
import com.adminportal.service.UserService;






@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger Log= LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	 private RoleRepository RoleRepository;


	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		// TODO Auto-generated method stub
		User localUser = userRepository.findByUsername(user.getUsername());
		
		if (localUser != null) {
			Log.info("user aready exists. nothing will be done.",user.getUsername());
		}else {
			for (UserRole userRole : userRoles) {
				RoleRepository.save(userRole.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			localUser = userRepository.save(user);
		}
		return localUser;
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

}
