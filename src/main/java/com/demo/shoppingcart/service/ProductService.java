package com.demo.shoppingcart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.shoppingcart.model.Product;
import com.demo.shoppingcart.repository.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepo productRepo;
	
	public List<Product> getAllProduct(){
		
		return productRepo.findAll();
	}

	public String deleteProduct(int id) {
		
		Product product=productRepo.findById(id).orElseThrow();
		productRepo.delete(product);
		return "Deleted";
	} 
	
	public Product addProduct(Product product) {
			
		return productRepo.save(product);
	}

	public Product findById(int id) {
		Product product=productRepo.findById(id).orElseThrow();
		return product;
	}

}
