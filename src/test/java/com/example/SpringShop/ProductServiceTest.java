package com.example.SpringShop;

import com.example.SpringShop.Dto.Product.ProductCreateDto;
import com.example.SpringShop.Entities.Category;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.Product;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.Exceptions.CategoryNotFoundException;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.ProductWithCustomerNotFoundException;
import com.example.SpringShop.Repositories.CategoryRepository;
import com.example.SpringShop.Repositories.CustomerRepository;
import com.example.SpringShop.Repositories.ProductRepository;
import com.example.SpringShop.Repositories.RecentlyViewedProductRepository;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import com.example.SpringShop.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest{
@Mock
private CustomerRepository customerRepository;

@Mock
private ProductRepository productRepository;

@Mock
private CategoryRepository categoryRepository;

@Mock
private RecentlyViewedProductRepository recentlyViewedProductRepository;

@Mock
private UserService userService;

@Mock
private CustomerService customerService;

@InjectMocks
private ProductService productService;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this); }
    @Test
    void testCreateProduct_Success() {
        ProductCreateDto productCreateDto = new ProductCreateDto("Title", "Description", "Location", "Available", "imageUrl", 100.0, "Category Name");

        Long customerId = 1L;
        Customer mockCustomer = new Customer();
        mockCustomer.setId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        Category mockCategory = new Category();
        mockCategory.setName("Category Name");
        when(categoryRepository.findByName("Category Name")).thenReturn(mockCategory);

        Product createdProduct = productService.createProduct(customerId, productCreateDto);

        assertNotNull(createdProduct);
        assertEquals("Title", createdProduct.getTitle());
        assertEquals("Description", createdProduct.getDescription());
        assertEquals("Location", createdProduct.getLocation());
        assertEquals("Available", createdProduct.getStatus());
        assertEquals("imageUrl", createdProduct.getImageUrl());
        assertEquals(100.0, createdProduct.getPrice());
        assertEquals(mockCustomer, createdProduct.getCustomer());
        assertEquals(mockCategory, createdProduct.getCategory());

    }

    @Test
    void testCreateProduct_CategoryNotFound() {
        ProductCreateDto productCreateDto = new ProductCreateDto("Title", "Description", "Location", "Available", "imageUrl", 100.0, "Category Name");

        Long customerId = 1L;
        Customer mockCustomer = new Customer();
        mockCustomer.setId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(categoryRepository.findByName("Invalid Category")).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> {
            productService.createProduct(customerId, productCreateDto);
        });
    }

    @Test
    void testDeactivateProduct_Success() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        var customer = new Customer();
        customer.setUser(user);
        customer.setId(1L);
        customer.setName("Test Customer");
        var product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setActive(true);
        product.setCustomer(customer);
        when(customerService.getCustomerByUsername(username)).thenReturn(customer);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.ProductWithCustomerExists(product.getId(), customer)).thenReturn(product);

        productService.deactivateProduct(product.getId(), username);

        assertFalse(product.isActive());
        verify(productRepository).save(product);
    }

    @Test
    void testDeactivateProduct_ProductNotFoundForCustomer() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        var customer = new Customer();
        customer.setUser(user);
        customer.setId(1L);
        customer.setName("Test Customer");
        var product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setActive(true);
        product.setCustomer(customer);
        when(customerService.getCustomerByUsername(username)).thenReturn(customer);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.ProductWithCustomerExists(product.getId(), customer)).thenReturn(null);

        assertThrows(ProductWithCustomerNotFoundException.class, () -> {
            productService.deactivateProduct(product.getId(), username);
        });
    }
}
