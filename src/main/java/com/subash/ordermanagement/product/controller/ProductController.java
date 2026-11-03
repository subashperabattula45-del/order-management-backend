package com.subash.ordermanagement.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subash.ordermanagement.product.dtos.ProductRequest;
import com.subash.ordermanagement.product.dtos.ProductResponse;
import com.subash.ordermanagement.product.dtos.ProductUpdateRequest;
import com.subash.ordermanagement.product.service.ProductService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {

        ProductResponse savedProduct = service.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products = service.getAllProducts();

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){

        ProductResponse product = service.getProductById(id);

        return ResponseEntity.status(HttpStatus.OK).body(product);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request){

            ProductResponse response = service.updateProduct(id, request);

            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){

        service.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
    
}
