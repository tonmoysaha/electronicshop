package com.electronicshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.electronicshop.entity.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long> {

}
