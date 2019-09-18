package com.electronicshop.entity.security;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.electronicshop.entity.User;

@Entity
public class PasswordResetToken {

	private static final int EXPIRARION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String token;

	private Date expiryDate;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	

	public PasswordResetToken() {
	}

	public PasswordResetToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
		this.expiryDate = calculateExpireyDate(EXPIRARION);
	}

	private Date calculateExpireyDate(final int expirarion2) {

		final Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, expirarion2);
		return new Date(cal.getTime().getTime());
	}

	public void updateToken(final String token) {
		this.token = token;
		this.expiryDate = calculateExpireyDate(EXPIRARION);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static int getExpirarion() {
		return EXPIRARION;
	}

}
