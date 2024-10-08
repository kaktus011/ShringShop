package com.example.SpringShop.EntityMappers;

import com.example.SpringShop.Dto.ProductViewDto;
import com.example.SpringShop.Dto.ProductDetailsDto;
import com.example.SpringShop.Entities.Product;

public class ProductMapper {

    public static ProductViewDto toProductViewDto(Product product) {
        return new ProductViewDto(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getImageUrl(),
                product.getLocation()
        );
    }

    public static ProductDetailsDto toProductDetailsDto(Product product) {
        return new ProductDetailsDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getName(),
                product.getImageUrl(),
                product.getStatus(),
                product.getLocation(),
                product.getCustomer().getId(),
                product.getCustomer().getName(),
                product.getCustomer().getMobileNumber()
        );
    }
}