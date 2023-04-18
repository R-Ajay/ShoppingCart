package com.demo.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.shoppingcart.model.Carts;

public interface CartRepo extends JpaRepository<Carts, Integer> {

}
