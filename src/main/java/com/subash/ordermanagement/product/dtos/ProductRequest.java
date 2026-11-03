package com.subash.ordermanagement.product.dtos;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRequest {
    
    @NotBlank(message = "Product name is required")
private String name;

private String description;

@NotNull(message = "Price is required")
@Positive(message = "Price must be greater than 0")
private BigDecimal price;

@NotNull(message = "Stock is required")
@PositiveOrZero(message = "Stock cannot be negative")
private Integer stock;

    public ProductRequest(){

    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public Integer getStock(){
        return stock;
    }

    public void setStock(Integer stock){
        this.stock = stock;
    }
}
