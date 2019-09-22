package com.adminportal.service;

import java.util.List;

import com.adminportal.entity.ElectronicProduct;

public interface ElectronicProductService {

	ElectronicProduct save(ElectronicProduct electronicProduct);
	
	List<ElectronicProduct> findAll();
	
	ElectronicProduct findById(Long id);
}
