package com.example.SpringShop;


import com.example.SpringShop.Controllers.HomeController;
import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Dto.Home.HomepageViewDto;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.UserNotFoundException;
import com.example.SpringShop.Services.CategoryService;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import com.example.SpringShop.Services.RecentSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Mock
    private RecentSearchService recentSearchService;

    @Mock
    private Authentication authentication;

    private String username = "testUser";
    private Long customerId = 1L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(username);
    }

    @Test
    public void testLoadHome_Success() {
        when(customerService.getCustomerId(username)).thenReturn(customerId);
        when(productService.getLast10ViewedProducts(customerId)).thenReturn(Collections.emptyList());
        when(recentSearchService.getRecentSearches(username)).thenReturn(Collections.emptyList());
        when(recentSearchService.getTop10MostSearched()).thenReturn(Collections.emptyList());
        when(categoryService.getAllCategoryNames()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = homeController.loadHome();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof HomepageViewDto);
    }

    @Test
    public void testLoadHome_CustomerNotFound() {
        when(customerService.getCustomerId(username)).thenThrow(new CustomerNotFoundException());

        ResponseEntity<?> response = homeController.loadHome();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals("Customer not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    public void testLoadHome_UserNotFound() {
        when(customerService.getCustomerId(username)).thenThrow(new UserNotFoundException());

        ResponseEntity<?> response = homeController.loadHome();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
        assertEquals("User not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }
}

