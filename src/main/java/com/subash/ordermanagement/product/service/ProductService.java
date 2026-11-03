package com.subash.ordermanagement.product.service;

import org.springframework.stereotype.Service;

import com.subash.ordermanagement.exception.ProductNotFoundException;
import com.subash.ordermanagement.product.dtos.ProductRequest;
import com.subash.ordermanagement.product.dtos.ProductResponse;
import com.subash.ordermanagement.product.dtos.ProductUpdateRequest;
import com.subash.ordermanagement.product.entity.Product;
import com.subash.ordermanagement.product.repository.ProductRepository;
import java.util.*;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product saved = repository.save(product);

        return new ProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getStock());
    }

    public List<ProductResponse> getAllProducts() {

        return repository.findAll()
                .stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock()))
                .toList();
    }

    public ProductResponse getProductById(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found with id" + id));

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock());
    }

    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found with id" + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product updatedProduct = repository.save(product);

        return new ProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getPrice(),
                updatedProduct.getStock());

    }

    public void deleteProduct(Long id){

        Product product = repository.findById(id)
                     .orElseThrow(() -> new ProductNotFoundException("product not found with id" + id));

        repository.delete(product);
    }

}
