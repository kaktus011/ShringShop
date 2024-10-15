package com.example.SpringShop;

import com.example.SpringShop.Controllers.ProductController;
import com.example.SpringShop.Dto.ErrorResponseDto;
import com.example.SpringShop.Dto.Product.ProductCreateDto;
import com.example.SpringShop.Dto.Product.ProductDetailsDto;
import com.example.SpringShop.Dto.Product.ProductIndexDto;
import com.example.SpringShop.Entities.*;
import com.example.SpringShop.EntityMappers.ProductMapper;
import com.example.SpringShop.Exceptions.*;
import com.example.SpringShop.Repositories.CategoryRepository;
import com.example.SpringShop.Repositories.ProductRepository;
import com.example.SpringShop.Repositories.RecentlyViewedProductRepository;
import com.example.SpringShop.Repositories.UserRepository;
import com.example.SpringShop.Services.CategoryService;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest{
@InjectMocks
private ProductController productController;

@Mock
private CustomerService customerService;

@Mock
private ProductService productService;

@Mock
private ProductRepository productRepository;

@Mock
private UserRepository userRepository;

@Mock
private CategoryRepository categoryRepository  ;

@Mock
private RecentlyViewedProductRepository recentlyViewedProductRepository;

@Mock
private ProductMapper productMapper;

private Authentication authentication;

@BeforeEach
public void setUp() {
    MockitoAnnotations.openMocks(this);
    authentication = mock(Authentication.class);
    SecurityContextHolder.getContext().setAuthentication(authentication);
}

    @Test
    void testCreateProduct_Success() {
        ProductCreateDto productCreateDto = new ProductCreateDto("Title", "Description", "Location", "Available", "imageUrl", 100.0, "Category Name");

        Category category = new Category();
        category.setName("Category Name");
        when(categoryRepository.findByName("Category Name")).thenReturn(category);

        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        Long customerId = 1L;
        when(customerService.getCustomerId(username)).thenReturn(customerId);

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setTitle(productCreateDto.getTitle());
        mockProduct.setDescription(productCreateDto.getDescription());
        mockProduct.setLocation(productCreateDto.getLocation());
        mockProduct.setStatus(productCreateDto.getStatus());
        mockProduct.setImageUrl(productCreateDto.getImage());
        mockProduct.setPrice(productCreateDto.getPrice());
        mockProduct.setView(0);
        mockProduct.setActive(true);
        mockProduct.setCreationDate(LocalDateTime.now());
        mockProduct.setCustomer(new Customer());
        mockProduct.setCategory(category);


        when(productService.createProduct(customerId, productCreateDto)).thenReturn(mockProduct);

        ResponseEntity<?> response = productController.createProduct(productCreateDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

@Test
void testCreateProduct_UserNotFound() {
    ProductCreateDto productCreateDto = new ProductCreateDto("Title", "Description", "Location", "Available", "imageUrl", 100.0, "Category Name");

    String username = "testUser";
    when(authentication.getName()).thenReturn(username);
    when(customerService.getCustomerId(username)).thenThrow(new UserNotFoundException());

    ResponseEntity<?> response = productController.createProduct(productCreateDto);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("User not found.", ((ErrorResponseDto) response.getBody()).getMessage());
}

@Test
void testCreateProduct_CustomerNotFound() {
    ProductCreateDto productCreateDto = new ProductCreateDto("Title", "Description", "Location", "Available", "imageUrl", 100.0, "Category Name");

    String username = "testUser";
    when(authentication.getName()).thenReturn(username);
    when(customerService.getCustomerId(username)).thenThrow(new CustomerNotFoundException());

    ResponseEntity<?> response = productController.createProduct(productCreateDto);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Customer not found.", ((ErrorResponseDto) response.getBody()).getMessage());
}

@Test
void testCreateProduct_CategoryNotFound() {
    ProductCreateDto productCreateDto = new ProductCreateDto("Title", "Description", "Location", "Available", "imageUrl", 100.0, "Category Name");

    String username = "testUser";
    when(authentication.getName()).thenReturn(username);
    Long customerId = 1L;
    when(customerService.getCustomerId(username)).thenReturn(customerId);
    when(productService.createProduct(customerId, productCreateDto))
            .thenThrow(new CategoryNotFoundException("Non-existent Category"));

    ResponseEntity<?> response = productController.createProduct(productCreateDto);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Category 'Non-existent Category' not found.", ((ErrorResponseDto) response.getBody()).getMessage());
}
    @Test
    public void testProductDetails_Success() {
        Long productId = 1L;
        String username = "testUser";
        ProductDetailsDto expectedProductDetails = new ProductDetailsDto();

        when(authentication.getName()).thenReturn(username);
        when(productService.productDetails(productId, username)).thenReturn(expectedProductDetails);

        ResponseEntity<?> response = productController.productDetails(productId);
        verify(productService, times(1)).productDetails(productId, username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProductDetails, response.getBody());
    }

    @Test
    public void testProductDetails_UserNotFoundException() {
        Long productId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        doThrow(new UserNotFoundException()).when(productService).productDetails(productId, username);

        ResponseEntity<?> response = productController.productDetails(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    public void testProductDetails_CustomerNotFoundException() {
        Long productId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        doThrow(new CustomerNotFoundException()).when(productService).productDetails(productId, username);

        ResponseEntity<?> response = productController.productDetails(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    public void testProductDetails_InvalidProductException() {
        Long productId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        doThrow(new InvalidProductException()).when(productService).productDetails(productId, username);

        ResponseEntity<?> response = productController.productDetails(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid product.", ((ErrorResponseDto) response.getBody()).getMessage());
    }


@Test
public void testDeactivateProduct_Success() {
    Long productId = 1L;
    String username = "testUser";

    when(authentication.getName()).thenReturn(username);

    ResponseEntity<?> response = productController.deactivateProduct(productId);
    verify(productService, times(1)).deactivateProduct(productId, username);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Product marked as inactive successfully.", response.getBody());
}

@Test
public void testDeactivateProduct_UserNotFoundException() {
    Long productId = 1L;
    String username = "testUser";

    when(authentication.getName()).thenReturn(username);
    doThrow(new UserNotFoundException()).when(productService).deactivateProduct(productId, username);

    ResponseEntity<?> response = productController.deactivateProduct(productId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("User not found.", ((ErrorResponseDto) response.getBody()).getMessage());
}

@Test
public void testDeactivateProduct_CustomerNotFoundException() {
    Long productId = 1L;
    String username = "testUser";

    when(authentication.getName()).thenReturn(username);
    doThrow(new CustomerNotFoundException()).when(productService).deactivateProduct(productId, username);

    ResponseEntity<?> response = productController.deactivateProduct(productId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Customer not found.", ((ErrorResponseDto) response.getBody()).getMessage());
}

@Test
public void testDeactivateProduct_InvalidProductException() {
    Long productId = 1L;
    String username = "testUser";

    when(authentication.getName()).thenReturn(username);
    doThrow(new InvalidProductException()).when(productService).deactivateProduct(productId, username);

    ResponseEntity<?> response = productController.deactivateProduct(productId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Invalid product.", ((ErrorResponseDto) response.getBody()).getMessage());
}

@Test
public void testDeactivateProduct_ProductWithCustomerNotFoundException() {
    Long productId = 1L;
    String username = "testUser";

    when(authentication.getName()).thenReturn(username);
    doThrow(new ProductWithCustomerNotFoundException()).when(productService).deactivateProduct(productId, username);

    ResponseEntity<?> response = productController.deactivateProduct(productId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Customer does not have access to this product.", ((ErrorResponseDto) response.getBody()).getMessage());
}
    @Test
    public void testUpdateProduct_Success() {
        Long productId = 1L;
Category category = new Category();
category.setName("Category Name");
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setTitle("Title");
        mockProduct.setDescription("Test description");
        mockProduct.setLocation("Test location");
        mockProduct.setStatus("Test status");
        mockProduct.setImageUrl("test");
        mockProduct.setPrice(20L);
        mockProduct.setView(0);
        mockProduct.setActive(true);
        mockProduct.setCreationDate(LocalDateTime.now());
        mockProduct.setCustomer(new Customer());
        mockProduct.setCategory(category);

        // Mock ProductCreateDto with updated values
        ProductCreateDto productCreateDto = new ProductCreateDto();
        productCreateDto.setTitle("Updated Title");
        productCreateDto.setDescription("Updated Description");
        productCreateDto.setPrice(150.0);
        productCreateDto.setLocation("Updated Location");
        productCreateDto.setCategory("Category Name");
        productCreateDto.setImage("updatedImageUrl");
        productCreateDto.setStatus("Available");

        // Mock repository methods
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(customerService.getCustomerByUsername("testUser")).thenReturn(new Customer());
        when(productRepository.ProductWithCustomerExists(productId, new Customer())).thenReturn(mockProduct);
        when(categoryRepository.findByName("Category Name")).thenReturn(new Category());

        // Call the update method
        Product updatedProduct = productService.updateProduct(productId, "testUser", productCreateDto);

        // Assertions to verify the updated product
        assertNotNull(updatedProduct);
        assertEquals("Updated Title", updatedProduct.getTitle());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(150.0, updatedProduct.getPrice());
        assertEquals("Updated Location", updatedProduct.getLocation());
        assertEquals("updatedImageUrl", updatedProduct.getImageUrl());
        assertEquals("Available", updatedProduct.getStatus());

        // Verify that the product is saved
        verify(productRepository).save(updatedProduct);
    }


    @Test
    public void testUpdateProduct_UserNotFoundException() {
        Long productId = 1L;
        var productCreateDto = new ProductCreateDto();
        when(productService.updateProduct(productId, "testUser", productCreateDto)).thenThrow(new UserNotFoundException());

        ResponseEntity<?> response = productController.updateProduct(productId, productCreateDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    public void testUpdateProduct_ProductWithCustomerNotFoundException() {
        Long productId = 1L;
        var productCreateDto = new ProductCreateDto();

        when(productService.updateProduct(productId, "testUser", productCreateDto)).thenThrow(new ProductWithCustomerNotFoundException());

        ResponseEntity<?> response = productController.updateProduct(productId, productCreateDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product with customer not found", ((ErrorResponseDto) response.getBody()).getMessage());
    }
}

