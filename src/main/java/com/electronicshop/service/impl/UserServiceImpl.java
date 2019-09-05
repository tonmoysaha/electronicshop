package com.electronicshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.electronicshop.entity.User;
import com.electronicshop.entity.security.PasswordResetToken;
import com.electronicshop.repository.PasswordResetTokenRepository;
import com.electronicshop.service.UserService;

public class UserServiceImpl implements UserService {
	
	 @Autowired
	 private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Override 
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetToken(User user, String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
		
	}

}
