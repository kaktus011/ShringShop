package com.example.SpringShop;

import com.example.SpringShop.Controllers.FavouritesController;
import com.example.SpringShop.Controllers.ProductController;
import com.example.SpringShop.Dto.ErrorResponseDto;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.InvalidProductException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FavouritesControllerTest {

    @InjectMocks
    private FavouritesController favouritesController;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testUser");
    }
    @Test
    void testMakeProductFavourite_Success() {
        Long productId = 1L;
        User user = new User();
        user.setUsername("testUser");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUser(user);

        when(customerService.getCustomerByUsername("testUser")).thenReturn(customer);
        doNothing().when(productService).makeProductFavourite(any(Customer.class), eq(productId));

        ResponseEntity<?> response = favouritesController.makeProductFavourite(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product added to favourites!", response.getBody());
        verify(customerService).getCustomerByUsername("testUser");
        verify(productService).makeProductFavourite(customer, productId);
    }

    @Test
    void testMakeProductFavourite_CustomerNotFound() {
        Long productId = 1L;

        when(customerService.getCustomerByUsername("testUser")).thenThrow(new CustomerNotFoundException());

        ResponseEntity<?> response = favouritesController.makeProductFavourite(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = (ErrorResponseDto) response.getBody();
        assertEquals("Customer not found.", errorResponse.getMessage());
    }

    @Test
    void testMakeProductFavourite_InvalidProduct() {
        Long productId = 1L;
        User user = new User();
        user.setUsername("testUser");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUser(user);

        when(customerService.getCustomerByUsername("testUser")).thenReturn(customer);
        doThrow(new InvalidProductException())
                .when(productService).makeProductFavourite(any(Customer.class), eq(productId));

        ResponseEntity<?> response = favouritesController.makeProductFavourite(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponseDto errorResponse = (ErrorResponseDto) response.getBody();
        assertEquals("Invalid product.", errorResponse.getMessage());
    }

    @Test
    void testMakeProductFavourite_UnhandledException() {
        Long productId = 1L;
        User user = new User();
        user.setUsername("testUser");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUser(user);

        when(customerService.getCustomerByUsername("testUser")).thenReturn(customer);
        doThrow(new RuntimeException("Unexpected error")).when(productService).makeProductFavourite(any(Customer.class), eq(productId));

        ResponseEntity<?> response = favouritesController.makeProductFavourite(productId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while making a product favourite.", response.getBody());
    }
}

