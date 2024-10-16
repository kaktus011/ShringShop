package com.example.SpringShop;
import com.example.SpringShop.Controllers.CategoryController;
import com.example.SpringShop.Dto.Category.CreateCategoryDto;
import com.example.SpringShop.Dto.Category.CategoryOverviewDto;
import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Entities.Category;
import com.example.SpringShop.Exceptions.CategoryAlreadyTakenException;
import com.example.SpringShop.Exceptions.CategoryNotFoundException;
import com.example.SpringShop.Exceptions.NewCategoryNameSameLikeOldNameException;
import com.example.SpringShop.Services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories_Success() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        when(categoryService.getAllCategories()).thenReturn(List.of(category));

        ResponseEntity<?> response = categoryController.getAllCategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<CategoryOverviewDto> dtoList = (List<CategoryOverviewDto>) response.getBody();
        assertEquals(1, dtoList.size());
        assertEquals("Test Category", dtoList.get(0).name);
    }

    @Test
    void testCreateCategory_Success() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("New Category");

        Category category = new Category();
        category.setId(1L);
        category.setName("New Category");

        when(categoryService.createCategory(any(CreateCategoryDto.class))).thenReturn(category);

        ResponseEntity<CategoryOverviewDto> response = categoryController.createCategory(createCategoryDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CategoryOverviewDto dto = response.getBody();
        assertEquals("New Category", dto.name);
    }

    @Test
    void testUpdateCategory_Success() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Updated Category");

        Category category = new Category();
        category.setId(1L);
        category.setName("Updated Category");

        when(categoryService.updateCategory(any(CreateCategoryDto.class), anyLong())).thenReturn(category);

        ResponseEntity<?> response = categoryController.updateCategory(1L, createCategoryDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CategoryOverviewDto dto = (CategoryOverviewDto) response.getBody();
        assertEquals("Updated Category", dto.name);
    }

    @Test
    void testUpdateCategory_CategoryNotFound() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Updated Category");

        when(categoryService.updateCategory(any(CreateCategoryDto.class), anyLong())).thenThrow(new CategoryNotFoundException("Category not found"));

        ResponseEntity<?> response = categoryController.updateCategory(1L, createCategoryDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = (ErrorResponseDto) response.getBody();
        assertEquals("Category 'Category not found' not found.", errorResponse.getMessage());
    }

    @Test
    void testUpdateCategory_NewCategoryNameSameAsOldName() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Old Category");

        when(categoryService.updateCategory(any(CreateCategoryDto.class), anyLong())).thenThrow(new NewCategoryNameSameLikeOldNameException());

        ResponseEntity<?> response = categoryController.updateCategory(1L, createCategoryDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = (ErrorResponseDto) response.getBody();
        assertEquals("New category name should be different from old category name.", errorResponse.getMessage());
    }

    @Test
    void testUpdateCategory_CategoryAlreadyTaken() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Taken Category");

        when(categoryService.updateCategory(any(CreateCategoryDto.class), anyLong())).thenThrow(new CategoryAlreadyTakenException("Taken Category"));

        ResponseEntity<?> response = categoryController.updateCategory(1L, createCategoryDto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponseDto errorResponse = (ErrorResponseDto) response.getBody();
        assertEquals("Category name 'Taken Category' is already taken.", errorResponse.getMessage());
    }

    @Test
    void testDeleteCategory_Success() {
        ResponseEntity<?> response = categoryController.deleteCategory(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category was successfully deleted!", response.getBody());
        verify(categoryService).deleteCategory(1L);
    }
}

