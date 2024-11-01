package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.Category.CategoryOverviewDto;
import com.example.SpringShop.Dto.Category.CreateCategoryDto;
import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Entities.Category;
import com.example.SpringShop.EntityMappers.CategoryMapper;
import com.example.SpringShop.Exceptions.CategoryAlreadyTakenException;
import com.example.SpringShop.Exceptions.CategoryNotFoundException;
import com.example.SpringShop.Exceptions.NewCategoryNameSameLikeOldNameException;
import com.example.SpringShop.Services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @GetMapping()
    public ResponseEntity<?> getAllCategories() {
        var categories = categoryService.getAllCategories();
        return ResponseEntity.ok(CategoryMapper.toCategoryOverviewDtoList(categories));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CategoryOverviewDto> createCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        Category category = categoryService.createCategory(createCategoryDto);
        return ResponseEntity.ok(CategoryMapper.toCategoryOverviewDto(category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CreateCategoryDto createCategoryDto) {
        try {
            Category updatedCategory = categoryService.updateCategory(createCategoryDto, id);
            return ResponseEntity.ok(CategoryMapper.toCategoryOverviewDto(updatedCategory));
        } catch (CategoryNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (NewCategoryNameSameLikeOldNameException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (CategoryAlreadyTakenException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category was successfully deleted!");
    }


}
