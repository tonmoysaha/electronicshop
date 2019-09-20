package com.adminportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.ElectronicProduct;
import com.adminportal.repository.ElectronicProductRepository;
import com.adminportal.service.ElectronicProductService;

@Service
public class ElectronicProductServiceImpl implements ElectronicProductService {
	
	@Autowired
	private ElectronicProductRepository ElectronicProductRepository;

	@Override
	public ElectronicProduct save(ElectronicProduct electronicProduct) {
		// TODO Auto-generated method stub
		
		return ElectronicProductRepository.save(electronicProduct);
	}

	@Override
	public List<ElectronicProduct> findAll() {
		// TODO Auto-generated method stub
		return (List<ElectronicProduct>) ElectronicProductRepository.findAll();
	}

	

}
