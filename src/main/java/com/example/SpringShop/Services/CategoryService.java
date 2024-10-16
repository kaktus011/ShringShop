package com.example.SpringShop.Services;


import com.example.SpringShop.Dto.Category.CategoryOverviewDto;
import com.example.SpringShop.Dto.Category.CreateCategoryDto;
import com.example.SpringShop.Exceptions.CategoryAlreadyTakenException;
import com.example.SpringShop.Exceptions.CategoryNotFoundException;
import com.example.SpringShop.Exceptions.NewCategoryNameSameLikeOldNameException;
import com.example.SpringShop.Exceptions.ProductsWithCategoryExistsException;
import com.example.SpringShop.Repositories.ProductRepository;
import org.springframework.stereotype.Service;


import com.example.SpringShop.Entities.Category;
import com.example.SpringShop.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
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

    public Category createCategory(CreateCategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return categoryRepository.save(category);
    }

    public Category updateCategory(CreateCategoryDto createCategoryDto, Long id) {
        Category category = categoryRepository.findById(id).get();
        if(category == null) {
            throw new CategoryNotFoundException(createCategoryDto.getName());
        }
        if(category.getName().equals(createCategoryDto.getName())) {
            throw new NewCategoryNameSameLikeOldNameException();
        }
        Category takenCategory = categoryRepository.findByName(createCategoryDto.getName());
        if(takenCategory != null) {
            throw new CategoryAlreadyTakenException(createCategoryDto.getName());
        }
        category.setName(createCategoryDto.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).get();
        if(category == null) {
            throw new CategoryNotFoundException(category.getName());
        }
        if(productRepository.productsWithCategoryExist(id)){
            throw new ProductsWithCategoryExistsException(category.getName());
        }
        categoryRepository.delete(category);
    }

}
