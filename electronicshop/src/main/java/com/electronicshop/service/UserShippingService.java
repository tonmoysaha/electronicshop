package com.electronicshop.service;

import com.electronicshop.entity.User;
import com.electronicshop.entity.UserShipping;

public interface UserShippingService {

	UserShipping findById(Long userShippingId);

	void setDefaultUserShipping(Long defaultUserShippingAddressId, User user);

	void deleteById(Long userShippingId);
	

}
