package com.electronicshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.electronicshop.entity.ElectronicProduct;

public interface ElectronicProductRepository extends CrudRepository<ElectronicProduct, Long> {

}
