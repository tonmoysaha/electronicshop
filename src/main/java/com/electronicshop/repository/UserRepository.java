package com.electronicshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.electronicshop.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
}
