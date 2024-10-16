package com.example.SpringShop.EntityMappers;

import com.example.SpringShop.Dto.Category.CategoryOverviewDto;
import com.example.SpringShop.Entities.Category;
import com.example.SpringShop.Exceptions.CategoryNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryOverviewDto toCategoryOverviewDto(Category category) {
        if (category == null) {
            throw new CategoryNotFoundException(category.getName());
        }
        return new CategoryOverviewDto(category.getId(), category.getName());
    }

    public static List<CategoryOverviewDto> toCategoryOverviewDtoList(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }
        return categories.stream()
                .map(CategoryMapper::toCategoryOverviewDto)
                .collect(Collectors.toList());
    }
}
