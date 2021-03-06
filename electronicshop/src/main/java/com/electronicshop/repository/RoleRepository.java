package com.electronicshop.repository;


import org.springframework.data.repository.CrudRepository;

import com.electronicshop.entity.security.Role;


public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String name);
}
