package com.example.SpringShop.Services;

import com.example.SpringShop.Dto.Cart.CartViewDto;
import com.example.SpringShop.Entities.Cart;
import com.example.SpringShop.Entities.Product;
import com.example.SpringShop.EntityMappers.CartMapper;
import com.example.SpringShop.Exceptions.CartNotFoundException;
import com.example.SpringShop.Exceptions.ProductNotInCartException;
import com.example.SpringShop.Repositories.CartRepository;
import com.example.SpringShop.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public CartViewDto getCartForCustomer(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);
        return CartMapper.toCartViewDto(cart, customerId);
    }

    public CartViewDto addProductToCart(Long customerId, Long productId) {
        Cart cart = getCartByCustomerId(customerId);
        Product productToAdd = productService.getProductById(productId);

        if (cart.getProducts().stream().anyMatch(product -> product.getId().equals(productId))) {
            return CartMapper.toCartViewDto(cart, customerId);
        }

        if (productToAdd.getCustomer().getId().equals(customerId)) {
            return CartMapper.toCartViewDto(cart, customerId);
        }

        cart.getProducts().add(productToAdd);
        cart.setTotalPrice(cart.getProducts().stream().mapToDouble(Product::getPrice).sum());
        cartRepository.save(cart);

        return CartMapper.toCartViewDto(cart, customerId);
    }

    public CartViewDto deleteProductFromCart(Long customerId, Long productId) {
        Cart cart = getCartByCustomerId(customerId);
        Product productToDelete = productService.getProductById(productId);

        if (!cart.getProducts().contains(productToDelete)) {
            throw new ProductNotInCartException("Product not in cart.");
        }

        cart.getProducts().remove(productToDelete);
        cart.setTotalPrice(cart.getProducts().stream().mapToDouble(Product::getPrice).sum());
        cartRepository.save(cart);

        return CartMapper.toCartViewDto(cart, customerId);
    }

    public CartViewDto clearCart(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.setProducts(new ArrayList<>());
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        return CartMapper.toCartViewDto(cart, customerId);
    }

    public Cart getCartByCustomerId(Long customerId) {
        Cart cart = cartRepository.findByCustomer_Id(customerId);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cart;
    }


}
