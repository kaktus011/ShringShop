package com.example.SpringShop;

import com.example.SpringShop.Dto.Category.CreateCategoryDto;
import com.example.SpringShop.Entities.Category;
import com.example.SpringShop.Exceptions.CategoryAlreadyTakenException;
import com.example.SpringShop.Exceptions.NewCategoryNameSameLikeOldNameException;
import com.example.SpringShop.Exceptions.ProductsWithCategoryExistsException;
import com.example.SpringShop.Repositories.CategoryRepository;
import com.example.SpringShop.Repositories.ProductRepository;
import com.example.SpringShop.Services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        Category category1 = new Category();
        category1.setName("Category 1");
        Category category2 = new Category();
        category2.setName("Category 2");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void testGetAllCategoryNames() {
        Category category1 = new Category();
        category1.setName("Category 1");
        Category category2 = new Category();
        category2.setName("Category 2");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<String> categoryNames = categoryService.getAllCategoryNames();

        assertEquals(2, categoryNames.size());
        assertEquals("Category 1", categoryNames.get(0));
        assertEquals("Category 2", categoryNames.get(1));
        verify(categoryRepository).findAll();
    }

    @Test
    void testUpdateCategory_SameName() {
        Long categoryId = 1L;
        CreateCategoryDto dto = new CreateCategoryDto();
        dto.setName("Old Category");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        assertThrows(NewCategoryNameSameLikeOldNameException.class, () -> categoryService.updateCategory(dto, categoryId));
    }

    @Test
    void testUpdateCategory_CategoryAlreadyTaken() {
        Long categoryId = 1L;
        CreateCategoryDto dto = new CreateCategoryDto();
        dto.setName("Taken Category");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findByName(dto.getName())).thenReturn(new Category());

        assertThrows(CategoryAlreadyTakenException.class, () -> categoryService.updateCategory(dto, categoryId));
    }

    @Test
    void testDeleteCategory_Success() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Category to delete");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.productsWithCategoryExist(categoryId)).thenReturn(false);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).delete(category);
    }


    @Test
    void testDeleteCategory_ProductsWithCategoryExists() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Category to delete");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.productsWithCategoryExist(categoryId)).thenReturn(true);

        assertThrows(ProductsWithCategoryExistsException.class, () -> categoryService.deleteCategory(categoryId));
    }
}

