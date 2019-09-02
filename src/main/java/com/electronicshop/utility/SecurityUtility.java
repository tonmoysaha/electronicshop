package com.electronicshop.utility;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtility {
	private static final String SALT = "salt";
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}
	
	@Bean 
	public static String randomPassword() {
		String SALTCHART =  "ABCEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat()*SALTCHART.length());
			salt.append(SALTCHART.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}
}
