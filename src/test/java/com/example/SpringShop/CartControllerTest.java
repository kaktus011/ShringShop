package com.example.SpringShop;

import com.example.SpringShop.Controllers.CartController;
import com.example.SpringShop.Dto.Cart.CartViewDto;
import com.example.SpringShop.Dto.ErrorResponseDto;
import com.example.SpringShop.Exceptions.CartNotFoundException;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Services.CartService;
import com.example.SpringShop.Services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    @Mock
    private CartService cartService;

    @Mock
    private CustomerService customerService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCartForCustomer_Success() throws CustomerNotFoundException, CartNotFoundException {
        String username = "test";
        Long customerId = 1L;
        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCustomerId(customerId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        when(customerService.getCustomerId(username)).thenReturn(customerId);
        when(cartService.getCartForCustomer(customerId)).thenReturn(cartViewDto);

        ResponseEntity<?> response = cartController.getCartForCustomer();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartViewDto, response.getBody());
    }

    @Test
    public void testGetCartForCustomer_CustomerNotFound() throws CustomerNotFoundException {
        String username = "test";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        when(customerService.getCustomerId(username)).thenThrow(new CustomerNotFoundException());

        ResponseEntity<?> response = cartController.getCartForCustomer();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorResponseDto.class, response.getBody().getClass());
    }

    @Test
    public void testAddProductToCart_Success() throws Exception {
        String username = "test";
        Long customerId = 1L;
        Long productId = 2L;
        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCustomerId(customerId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        when(customerService.getCustomerId(username)).thenReturn(customerId);
        when(cartService.addProductToCart(customerId, productId)).thenReturn(cartViewDto);

        ResponseEntity<?> response = cartController.addProductToCart(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartViewDto, response.getBody());
    }
}
