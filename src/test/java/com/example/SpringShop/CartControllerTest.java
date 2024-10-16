package com.example.SpringShop;

import com.example.SpringShop.Controllers.CartController;
import com.example.SpringShop.Dto.Cart.CartViewDto;
import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Exceptions.CartNotFoundException;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.InvalidProductException;
import com.example.SpringShop.Exceptions.UserNotFoundException;
import com.example.SpringShop.Repositories.CartRepository;
import com.example.SpringShop.Services.CartService;
import com.example.SpringShop.Services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CartControllerTest {
    @Mock
    private CartRepository cartRepository;
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
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetCartForCustomer_WhenCartIsFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        Long customerId = 1L;
        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCartId(1L);
        cartViewDto.setCustomerId(customerId);
        cartViewDto.setTotalPrice(0.0);
        cartViewDto.setProducts(new ArrayList<>());

        when(customerService.getCustomerId("testUser")).thenReturn(customerId);
        when(cartService.getCartForCustomer(customerId)).thenReturn(cartViewDto);

        ResponseEntity<?> response = cartController.getCartForCustomer();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartViewDto, response.getBody());
    }

    @Test
    public void testGetCartForCustomer_WhenCustomerNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        when(customerService.getCustomerId("testUser")).thenThrow(new CustomerNotFoundException());

        ResponseEntity<?> response = cartController.getCartForCustomer();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    public void testGetCartForCustomer_WhenOtherExceptionOccurs() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        Long customerId = 1L;
        when(customerService.getCustomerId("testUser")).thenReturn(customerId);
        when(cartService.getCartForCustomer(customerId)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = cartController.getCartForCustomer();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred with retrieving your cart.", response.getBody());
    }
    @Test
    public void testGetCartForCustomer_WhenCartIsNull() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        Long customerId = 1L;

        when(customerService.getCustomerId("testUser")).thenReturn(customerId);
        when(cartService.getCartForCustomer(customerId)).thenReturn(null);

        ResponseEntity<?> response = cartController.getCartForCustomer();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }
    @Test
    public void testAddProductToCart_WhenProductAddedSuccessfully() {
        Long customerId = 1L;
        Long productId = 2L;
        CartViewDto expectedCartViewDto = new CartViewDto();

        when(customerService.getCustomerId(anyString())).thenReturn(customerId);
        when(cartService.addProductToCart(customerId, productId)).thenReturn(expectedCartViewDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "password"));

        ResponseEntity<?> response = cartController.addProductToCart(productId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedCartViewDto, response.getBody());
    }

    @Test
    public void testAddProductToCart_WhenCustomerNotFound() {
        Long productId = 2L;
        when(customerService.getCustomerId(anyString())).thenThrow(new CustomerNotFoundException());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "password"));

        ResponseEntity<?> response = cartController.addProductToCart(productId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Customer not found.", response.getBody());
    }

    @Test
    public void testAddProductToCart_WhenInvalidProduct() {
        Long customerId = 1L;
        Long productId = 2L;
        when(customerService.getCustomerId(anyString())).thenReturn(customerId);
        when(cartService.addProductToCart(customerId, productId)).thenThrow(new InvalidProductException());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "password"));

        ResponseEntity<?> response = cartController.addProductToCart(productId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Invalid product.", response.getBody());
    }

    @Test
    public void testAddProductToCart_WhenCartNotFound() {
        Long customerId = 1L;
        Long productId = 2L;

        when(customerService.getCustomerId(anyString())).thenReturn(customerId);
        when(cartService.addProductToCart(customerId, productId)).thenThrow(new CartNotFoundException());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "password"));

        ResponseEntity<?> response = cartController.addProductToCart(productId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Cart not found.", response.getBody());
    }

    @Test
    public void testAddProductToCart_WhenOtherExceptionOccurs() {
        Long customerId = 1L;
        Long productId = 2L;

        when(customerService.getCustomerId(anyString())).thenReturn(customerId);
        when(cartService.addProductToCart(customerId, productId)).thenThrow(new RuntimeException("Unexpected error"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "password"));

        ResponseEntity<?> response = cartController.addProductToCart(productId);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred with adding a product to the cart", response.getBody());
    }
    @Test
    void testDeleteProductFromCart_Success() {
        Long productId = 1L;
        Long customerId = 1L;
        var cartViewDto = new CartViewDto();
        cartViewDto.setCartId(1L);
        cartViewDto.setCustomerId(1L);
        cartViewDto.setTotalPrice(100.0);

        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(customerService.getCustomerId("testUser")).thenReturn(customerId);
        when(cartService.deleteProductFromCart(eq(customerId), eq(productId))).thenReturn(cartViewDto);

        ResponseEntity<?> response = cartController.deleteProductFromCart(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartViewDto, response.getBody());
        verify(cartService).deleteProductFromCart(customerId, productId);
    }

    @Test
    void testDeleteProductFromCart_CustomerNotFound() {
        Long productId = 1L;

        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(customerService.getCustomerId("testUser")).thenThrow(new CustomerNotFoundException());

        ResponseEntity<?> response = cartController.deleteProductFromCart(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer not found.", response.getBody());
    }

    @Test
    void testDeleteProductFromCart_UserNotFound() {
        Long productId = 1L;
        Long customerId = 1L;

        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(customerService.getCustomerId("testUser")).thenReturn(customerId);
        when(cartService.deleteProductFromCart(eq(customerId), eq(productId))).thenThrow(new UserNotFoundException());

        ResponseEntity<?> response = cartController.deleteProductFromCart(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
    }

    @Test
    void testDeleteProductFromCart_InvalidProduct() {
        Long productId = 1L;
        Long customerId = 1L;

        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(customerService.getCustomerId("testUser")).thenReturn(customerId);
        when(cartService.deleteProductFromCart(eq(customerId), eq(productId))).thenThrow(new InvalidProductException());

        ResponseEntity<?> response = cartController.deleteProductFromCart(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid product.", response.getBody());
    }

    @Test
    void testDeleteProductFromCart_CartNotFound() {
        Long productId = 1L;
        Long customerId = 1L;

        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(customerService.getCustomerId("testUser")).thenReturn(customerId);
        when(cartService.deleteProductFromCart(eq(customerId), eq(productId))).thenThrow(new CartNotFoundException());

        ResponseEntity<?> response = cartController.deleteProductFromCart(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart not found.", response.getBody());
    }

    @Test
    void testDeleteProductFromCart_OtherException() {
        Long productId = 1L;
        Long customerId = 1L;

        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(customerService.getCustomerId("testUser")).thenReturn(customerId);
        when(cartService.deleteProductFromCart(eq(customerId), eq(productId))).thenThrow(new RuntimeException("Some error"));

        ResponseEntity<?> response = cartController.deleteProductFromCart(productId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred with deleting a product from the cart", response.getBody());
    }

    @Test
    public void testClearCart_Success() throws Exception {
        Long customerId = 1L;
        String username = "testUser";
        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCartId(1L);
        cartViewDto.setProducts(new ArrayList<>());
        cartViewDto.setCustomerId(customerId);
        cartViewDto.setTotalPrice(0.0);

        when(customerService.getCustomerId(username)).thenReturn(customerId);
        when(cartService.clearCart(customerId)).thenReturn(cartViewDto);

        ResponseEntity<?> response = cartController.clearCart();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartViewDto, response.getBody());
    }

    @Test
    public void testClearCart_CustomerNotFound() throws Exception {
        Long customerId = 1L;
        String username = "testUser";
        when(customerService.getCustomerId(username)).thenThrow(new CustomerNotFoundException());

        ResponseEntity<?> response = cartController.clearCart();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer not found.", response.getBody());
    }

    @Test
    public void testClearCart_CartNotFound() throws Exception {
        Long customerId = 1L;
        String username = "testUser";
        when(customerService.getCustomerId(username)).thenReturn(customerId);
        when(cartService.clearCart(customerId)).thenThrow(new CartNotFoundException());

        ResponseEntity<?> response = cartController.clearCart();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart not found.", response.getBody());
    }

    @Test
    public void testClearCart_InternalServerError() throws Exception {
        Long customerId = 1L;
        String username = "testUser";
        when(customerService.getCustomerId(username)).thenReturn(customerId);
        when(cartService.clearCart(customerId)).thenThrow(new RuntimeException("Some unexpected error"));

        ResponseEntity<?> response = cartController.clearCart();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred with clearing the cart", response.getBody());
    }
}

