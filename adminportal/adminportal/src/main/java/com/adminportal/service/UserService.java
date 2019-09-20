package com.adminportal.service;

import java.util.Set;

import com.adminportal.entity.User;
import com.adminportal.entity.security.UserRole;


public interface UserService {

	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);

}
