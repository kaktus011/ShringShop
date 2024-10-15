package com.example.SpringShop;

import com.example.SpringShop.Dto.Product.ProductCreateDto;
import com.example.SpringShop.Dto.Product.ProductDetailsDto;
import com.example.SpringShop.Dto.Product.ProductViewDto;
import com.example.SpringShop.Entities.*;
import com.example.SpringShop.Exceptions.*;
import com.example.SpringShop.Repositories.*;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import com.example.SpringShop.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
private CustomerFavouriteProductRepository customerFavouriteProductRepository;

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

    @Test
    void testGetFavouriteProducts_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setActive(true);
        product1.setPrice(99.99);
        product1.setImageUrl("image1.jpg");
        product1.setLocation("Location 1");
        product1.setCreationDate(LocalDateTime.now());
        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setActive(true);
        product2.setPrice(59.99);
        product2.setImageUrl("image2.jpg");
        product2.setLocation("Location 2");
        product2.setCreationDate(LocalDateTime.now());

        CustomerFavouriteProduct favourite1 = new CustomerFavouriteProduct();
        favourite1.setProduct(product1);

        CustomerFavouriteProduct favourite2 = new CustomerFavouriteProduct();
        favourite2.setProduct(product2);

        List<CustomerFavouriteProduct> favouriteProducts = Arrays.asList(favourite1, favourite2);

        when(customerFavouriteProductRepository.findByCustomerId(customer.getId())).thenReturn(favouriteProducts);

        List<ProductViewDto> result = productService.getFavouriteProducts(customer);
        assertEquals(2, result.size());
        ProductViewDto dto1 = result.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Product 1", dto1.getTitle());
        assertEquals(99.99, dto1.getPrice());
        assertEquals("image1.jpg", dto1.getImage());
        assertEquals("Location 1", dto1.getLocation());
        ProductViewDto dto2 = result.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Product 2", dto2.getTitle());
        assertEquals(59.99, dto2.getPrice());
        assertEquals("image2.jpg", dto2.getImage());
        assertEquals("Location 2", dto2.getLocation());

        verify(customerFavouriteProductRepository, times(1)).findByCustomerId(customer.getId());
    }

    @Test
    void testGetFavouriteProducts_EmptyList() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerFavouriteProductRepository.findByCustomerId(customer.getId())).thenReturn(Arrays.asList());
        List<ProductViewDto> result = productService.getFavouriteProducts(customer);
        assertEquals(0, result.size());
        verify(customerFavouriteProductRepository, times(1)).findByCustomerId(customer.getId());
    }

    @Test
    public void testProductDetails_NewlyViewedProduct() {
        var product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setDescription("This is a test product");
        product.setPrice(100.0);
        product.setView(0);
        Category category = new Category();
        category.setId(1L);
        category.setName("TestCategory");
        product.setCategory(category);
        var customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");
        User user = new User();
        user.setEmail("test@example.com");
        customer.setUser(user);
        product.setCustomer(customer);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerService.getCustomerByUsername("testuser")).thenReturn(customer);
        when(recentlyViewedProductRepository.findByProductIdAndCustomer(1L, customer)).thenReturn(Optional.empty());

        ProductDetailsDto result = productService.productDetails(1L, "testuser");

        assertEquals(1, product.getView());
        verify(recentlyViewedProductRepository).save(any(RecentlyViewedProduct.class));
        assertNotNull(result);
        assertEquals(product.getTitle(), result.getTitle());
        assertEquals(product.getDescription(), result.getDescription());
    }

    @Test
    public void testProductDetails_AlreadyViewedProduct() {
        var product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setDescription("This is a test product");
        product.setPrice(100.0);
        product.setView(0);
        Category category = new Category();
        category.setId(1L);
        category.setName("TestCategory");
        product.setCategory(category);
        var customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");
        User user = new User();
        user.setEmail("test@example.com");
        customer.setUser(user);
        product.setCustomer(customer);

        var recentlyViewedProduct = new RecentlyViewedProduct();
        recentlyViewedProduct.setProduct(product);
        recentlyViewedProduct.setCustomer(customer);
        recentlyViewedProduct.setViewedAt(LocalDateTime.now());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerService.getCustomerByUsername("testuser")).thenReturn(customer);
        when(recentlyViewedProductRepository.findByProductIdAndCustomer(1L, customer)).thenReturn(Optional.of(recentlyViewedProduct));

        ProductDetailsDto result = productService.productDetails(1L, "testuser");

        assertEquals(0, product.getView());
        assertNotNull(recentlyViewedProduct.getViewedAt());
        assertNotNull(result);
        assertEquals(product.getTitle(), result.getTitle());
        assertEquals(product.getDescription(), result.getDescription());
    }

    @Test
    public void testMakeProductFavourite_AddsToFavouriteSuccessfully() {
        // Act
        var customer = new Customer();
        customer.setId(1L);
        customer.setFavouriteProducts(new ArrayList<>());

        var product = new Product();
        product.setId(1L);
        product.setCustomer(new Customer());
        product.getCustomer().setId(2L);  // Another customer owns the product

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        productService.makeProductFavourite(customer, product.getId());

        // Assert
        assertEquals(1, customer.getFavouriteProducts().size());
        assertTrue(customer.getFavouriteProducts().get(0).getProduct().equals(product));
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testMakeProductFavourite_ThrowsExceptionWhenFavoritingOwnProduct() {
        // Arrange
        var customer = new Customer();
        customer.setId(1L);
        customer.setFavouriteProducts(new ArrayList<>());

       var product = new Product();
        product.setId(1L);
        product.setCustomer(new Customer());
        product.getCustomer().setId(2L);  // Another customer owns the product

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        product.setCustomer(customer); // Customer owns the product

        // Act & Assert
        assertThrows(CannotAddToFavouritesException.class, () -> {
            productService.makeProductFavourite(customer, product.getId());
        });

        // Ensure no interaction with the repository when exception is thrown
        verify(customerRepository, never()).save(customer);
    }

    @Test
    public void testMakeProductFavourite_DoesNotAddDuplicate() {
        // Arrange
        var customer = new Customer();
        customer.setId(1L);
        customer.setFavouriteProducts(new ArrayList<>());

        var product = new Product();
        product.setId(1L);
        product.setCustomer(new Customer());
        product.getCustomer().setId(2L);  // Another customer owns the product

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        CustomerFavouriteProduct favouriteProduct = new CustomerFavouriteProduct();
        favouriteProduct.setProduct(product);
        customer.setFavouriteProducts(Collections.singletonList(favouriteProduct));

        // Act
        productService.makeProductFavourite(customer, product.getId());

        // Assert
        assertEquals(1, customer.getFavouriteProducts().size());  // No duplicates added
        verify(customerRepository, never()).save(customer); // No save as it's already favorited
    }
}
