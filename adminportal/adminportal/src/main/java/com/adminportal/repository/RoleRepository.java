package com.adminportal.repository;


import org.springframework.data.repository.CrudRepository;

import com.adminportal.entity.security.Role;


public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String name);
}
