package com.electronicshop.service;

import com.electronicshop.entity.User;
import com.electronicshop.entity.security.PasswordResetToken;

public interface UserService {

	PasswordResetToken getPasswordResetToken(final String token);

	void createPasswordResetToken(final User user, final String token);

}
