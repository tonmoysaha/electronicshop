package com.electronicshop.service;

import com.electronicshop.entity.UserPayment;

public interface UserPaymentService {
	
	UserPayment findById(Long id);

	void removeById(Long creditId);
	

}
