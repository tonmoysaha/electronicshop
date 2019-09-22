package com.electronicshop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicshop.entity.ElectronicProduct;
import com.electronicshop.repository.ElectronicProductRepository;
import com.electronicshop.service.ElectronicProductService;

@Service
public class ElectronicProductServiceImpl implements ElectronicProductService {
	
	@Autowired
	private ElectronicProductRepository ElectronicProductRepository;
	

	@Override
	public List<ElectronicProduct> findAll() {
		// TODO Auto-generated method stub
		return (List<ElectronicProduct>) ElectronicProductRepository.findAll();
	}


	@Override
	public ElectronicProduct findById(Long id) {
		// TODO Auto-generated method stub
		return ElectronicProductRepository.findById(id).orElse(null);
	}


	
}
