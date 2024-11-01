package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.Cart.CartViewDto;
import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Exceptions.*;
import com.example.SpringShop.Services.CartService;
import com.example.SpringShop.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/get-cart")
    public ResponseEntity<?> getCartForCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            Long customerId = customerService.getCustomerId(username);
            CartViewDto cartViewDto = cartService.getCartForCustomer(customerId);
            if (cartViewDto == null) {
                throw new CartNotFoundException();
            }
            return ResponseEntity.ok(cartViewDto);
        } catch (CustomerNotFoundException | UserNotFoundException | CartNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred with retrieving your cart.");
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/add-product/{id}")
    public ResponseEntity<?> addProductToCart(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Long customerId = customerService.getCustomerId(username);
            CartViewDto cartViewDto = cartService.addProductToCart(customerId, id);
            return ResponseEntity.ok(cartViewDto);
        } catch (CustomerNotFoundException | UserNotFoundException | InvalidProductException | CartNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred with adding a product to the cart");
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Long customerId = customerService.getCustomerId(username);
            CartViewDto cartViewDto = cartService.deleteProductFromCart(customerId, id);
            return ResponseEntity.ok(cartViewDto);
        } catch (CustomerNotFoundException | UserNotFoundException | InvalidProductException | CartNotFoundException |
                 ProductNotInCartException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred with deleting a product from the cart");
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/clear-cart")
    public ResponseEntity<?> clearCart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Long customerId = customerService.getCustomerId(username);
            CartViewDto cartViewDto = cartService.clearCart(customerId);
            return ResponseEntity.ok(cartViewDto);
        } catch (CustomerNotFoundException | UserNotFoundException | InvalidProductException | CartNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred with clearing the cart");
        }
    }
}
