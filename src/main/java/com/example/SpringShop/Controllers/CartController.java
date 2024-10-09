package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.CartViewDto;
import com.example.SpringShop.Entities.Cart;
import com.example.SpringShop.Services.CartService;
import com.example.SpringShop.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CustomerService customerService;

    @Autowired
    public CartController(CartService cartService, CustomerService customerService) {
        this.cartService = cartService;
        this.customerService = customerService;
    }

    @GetMapping("/get-cart")
    public ResponseEntity<CartViewDto> getCartForCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long customerId = customerService.getCustomerId(username);

        CartViewDto cartViewDto = cartService.getCartForCustomer(customerId);
        return ResponseEntity.ok(cartViewDto);
    }

    @PostMapping("/add-product/{id}")
    public ResponseEntity<?> addProductToCart(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long customerId = customerService.getCustomerId(username);

        Cart cart = cartService.addProductToCart(customerId, id);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long customerId = customerService.getCustomerId(username);

        Cart cart = cartService.deleteProductFromCart(customerId, id);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/clear-cart")
    public ResponseEntity<?> clearCart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long customerId = customerService.getCustomerId(username);

        Cart cart = cartService.clearcart(customerId);
        return ResponseEntity.ok(cart);
    }
}
