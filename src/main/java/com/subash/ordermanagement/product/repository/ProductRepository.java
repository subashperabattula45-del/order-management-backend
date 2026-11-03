package com.subash.ordermanagement.product.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.subash.ordermanagement.product.entity.Product;

public interface ProductRepository extends  JpaRepository<Product, Long> {
    
}
