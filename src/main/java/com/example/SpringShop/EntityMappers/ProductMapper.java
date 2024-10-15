package com.example.SpringShop.EntityMappers;

import com.example.SpringShop.Dto.Product.ProductIndexDto;
import com.example.SpringShop.Dto.Product.ProductViewDto;
import com.example.SpringShop.Dto.Product.ProductDetailsDto;
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
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getImageUrl(),
                product.getStatus(),
                product.getLocation(),
                product.getView() ,
                product.getCustomer().getId(),
                product.getCustomer().getName(),
                product.getCustomer().getMobileNumber(),
                product.getCustomer().getUser().getEmail(),
                product.getCustomersWhoFavourited().size()
        );
    }

    public static ProductIndexDto toProductIndexDto(Product product){
        ProductIndexDto dto = new ProductIndexDto();
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory().getName());
        dto.setCategoryId(product.getCategory().getId());
        dto.setStatus(product.getStatus());
        dto.setLocation(product.getLocation());
        dto.setImage(product.getImageUrl());
        dto.setProductId(product.getId());
        dto.setCustomerId(product.getCustomer().getId());
        dto.setViews(product.getView());
        dto.setActive(product.isActive());
        dto.setCreationDate(product.getCreationDate());
        dto.setCustomersWhoFavourited(product.getCustomersWhoFavourited().size());

        return dto;
    }
}