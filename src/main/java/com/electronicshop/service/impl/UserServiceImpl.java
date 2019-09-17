package com.electronicshop.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicshop.entity.User;
import com.electronicshop.entity.security.PasswordResetToken;
import com.electronicshop.entity.security.UserRole;
import com.electronicshop.repository.PasswordResetTokenRepository;
import com.electronicshop.repository.RoleRepository;
import com.electronicshop.repository.UserRepository;
import com.electronicshop.service.UserService;



@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	 private PasswordResetTokenRepository passwordResetTokenRepository;
	 
	 @Autowired
	 private RoleRepository RoleRepository;
	
	@Override 
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetToken(User user, String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
		
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		// TODO Auto-generated method stub
		User localUser = userRepository.findByUsername(user.getUsername());
		
		if (localUser != null) {
			throw new Exception("user aready exists. nothing will be done");
		}else {
			for (UserRole userRole : userRoles) {
				RoleRepository.save(userRole.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			localUser = userRepository.save(user);
		}
		return localUser;
	}

}
