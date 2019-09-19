package com.electronicshop.utility;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.electronicshop.entity.User;

@Component
public class MailConstructor {

	@Autowired
	private Environment env;

	public SimpleMailMessage constructResetToken(String contextPath, Locale locale, String token, User user,
			String password) {

		String url = contextPath + "/newuser?token=" + token;
		String message = "\nPlease click on this link to verify your email and edit your personal information. Your password is: \n"
				+ password;

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("ElectronicShop");
		email.setText(url + message);
		email.setFrom(env.getProperty("support.email"));

		return email;

	}
}
