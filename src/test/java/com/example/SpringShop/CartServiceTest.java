package com.example.SpringShop;

import com.example.SpringShop.Dto.Cart.CartViewDto;
import com.example.SpringShop.Entities.Cart;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.Product;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.Exceptions.CartNotFoundException;
import com.example.SpringShop.Exceptions.ProductNotInCartException;
import com.example.SpringShop.Repositories.CartRepository;
import com.example.SpringShop.Repositories.ProductRepository;
import com.example.SpringShop.Services.CartService;
import com.example.SpringShop.Services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    private Long customerId = 1L;
    private Cart cart;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        cart = new Cart();
        cart.setId(1L);
        cart.setProducts(new ArrayList<>());
    }

    @Test
    public void testGetCartForCustomer_Success() {
        when(cartRepository.findByCustomer_Id(customerId)).thenReturn(cart);

        CartViewDto result = cartService.getCartForCustomer(customerId);
        assertNotNull(result);
        assertEquals(cart.getId(), result.getCartId());
    }

    @Test
    public void testGetCartForCustomer_CartNotFound() {
        when(cartRepository.findByCustomer_Id(customerId)).thenReturn(null);

        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCartForCustomer(customerId);
        });
    }

    @Test
    public void testAddProductToCart_Success() {
        cart = new Cart();
        cart.setId(1L);
        cart.setProducts(new ArrayList<>());
        cart.setTotalPrice(0.0);

        var product = new Product();
        product.setId(1L);
        product.setPrice(100.0);
        Customer customer = new Customer();
        customer.setId(1L);
        Long productId = product.getId();
        product.setCustomer(customer);

        when(cartRepository.findByCustomer_Id(customerId)).thenReturn((cart));
        when(productService.getProductById(productId)).thenReturn(product);

        CartViewDto cartViewDto = cartService.addProductToCart(customerId, productId);
        assertEquals(cartViewDto.getTotalPrice(), cart.getTotalPrice());
    }

    @Test
    public void testAddProductToCart_ProductAlreadyInCart() {
        Product productToAdd = new Product();
        productToAdd.setId(1L);
        productToAdd.setPrice(100.0);
        cart.getProducts().add(productToAdd);

        when(cartRepository.findByCustomer_Id(customerId)).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(productToAdd);

        CartViewDto result = cartService.addProductToCart(customerId, 1L);
        assertEquals(1, cart.getProducts().size());
        assertEquals(cart.getId(), result.getCartId());
    }

    @Test
    public void testAddProductToCart_ProductBelongsToCustomer() {
        Product productToAdd = new Product();
        productToAdd.setId(1L);
        productToAdd.setPrice(100.0);
        Customer customer = new Customer();
        customer.setId(1L);
        productToAdd.setCustomer(customer);

        when(cartRepository.findByCustomer_Id(customerId)).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(productToAdd);

        CartViewDto result = cartService.addProductToCart(customerId, 1L);

        assertEquals(0, cart.getProducts().size());
        assertEquals(cart.getId(), result.getCartId());
    }

    @Test
    public void testDeleteProductFromCart_Success() {
        Product productToDelete = new Product();
        productToDelete.setId(1L);
        cart.getProducts().add(productToDelete);

        when(cartRepository.findByCustomer_Id(customerId)).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(productToDelete);

        CartViewDto result = cartService.deleteProductFromCart(customerId, 1L);
        assertEquals(0, cart.getProducts().size());
        assertEquals(cart.getId(), result.getCartId());
    }

    @Test
    public void testDeleteProductFromCart_ProductNotInCart() {
        Product productToDelete = new Product();
        productToDelete.setId(1L);

        when(cartRepository.findByCustomer_Id(customerId)).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(productToDelete);

        assertThrows(ProductNotInCartException.class, () -> {
            cartService.deleteProductFromCart(customerId, 1L);
        });
    }

    @Test
    public void testClearCart_Success() {
        when(cartRepository.findByCustomer_Id(customerId)).thenReturn(cart);

        CartViewDto result = cartService.clearCart(customerId);

        assertEquals(0, cart.getProducts().size());
        assertEquals(cart.getId(), result.getCartId());
    }
}
