package com.electronicshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicshop.entity.User;
import com.electronicshop.entity.UserShipping;
import com.electronicshop.repository.UserShippingRepository;
import com.electronicshop.service.UserShippingService;
@Service
public class UserShippingServiceImpl implements UserShippingService {
	
	@Autowired
	private UserShippingRepository UserShippingRepository;

	@Override
	public UserShipping findById(Long userShippingId) {
		// TODO Auto-generated method stub
		return UserShippingRepository.findById(userShippingId).orElse(null);
	}


	@Override
	public void setDefaultUserShipping(Long defaultUserShippingAddressId, User user) {
		// TODO Auto-generated method stub
	List<UserShipping> userShippings = (List<UserShipping>) UserShippingRepository.findAll();
		
		for (UserShipping userShipping : userShippings) {
			if (userShipping.getId() == defaultUserShippingAddressId) {
				userShipping.setDefaultShippingAddress(true);
				UserShippingRepository.save(userShipping);
			}else {
				userShipping.setDefaultShippingAddress(false);
				UserShippingRepository.save(userShipping);
			}
		}
		
	}


	@Override
	public void deleteById(Long userShippingId) {
		UserShippingRepository.deleteById(userShippingId);
		
	}

}
