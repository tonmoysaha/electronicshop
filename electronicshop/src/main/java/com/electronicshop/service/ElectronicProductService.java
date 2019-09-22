package com.electronicshop.service;

import java.util.List;
import java.util.Optional;

import com.electronicshop.entity.ElectronicProduct;

public interface ElectronicProductService {
	
	List<ElectronicProduct> findAll();

	ElectronicProduct findById(Long id);

}
