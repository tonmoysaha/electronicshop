package com.electronicshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.electronicshop.entity.UserPayment;
import com.electronicshop.repository.UserPaymentRepository;
import com.electronicshop.service.UserPaymentService;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {
	
	@Autowired
	private UserPaymentRepository UserPaymentRepository;

	@Override
	public UserPayment findById(Long id) {
		// TODO Auto-generated method stub
		return UserPaymentRepository.findById(id).orElse(null);
	}

	@Override
	public void removeById(Long creditId) {
	       UserPaymentRepository.deleteById(creditId);
		
	}

}
