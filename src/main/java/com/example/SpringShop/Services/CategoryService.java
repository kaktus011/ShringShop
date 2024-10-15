package com.example.SpringShop.Services;


import org.springframework.stereotype.Service;


import com.example.SpringShop.Entities.Category;
import com.example.SpringShop.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<String> getAllCategoryNames() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .limit(10)
                .collect(Collectors.toList());
    }

}
