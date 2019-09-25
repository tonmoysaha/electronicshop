package com.electronicshop.service;

import java.util.Set;

import com.electronicshop.entity.User;
import com.electronicshop.entity.UserBilling;
import com.electronicshop.entity.UserPayment;
import com.electronicshop.entity.security.PasswordResetToken;
import com.electronicshop.entity.security.UserRole;

public interface UserService {

	PasswordResetToken getPasswordResetToken(final String token);

	void createPasswordResetToken(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);
	
	void updateUserBilling(UserBilling userBilling,UserPayment userPayment , User user);

	void setUserDefaultPayment(Long defaultUserPaymentId, User user);

}
