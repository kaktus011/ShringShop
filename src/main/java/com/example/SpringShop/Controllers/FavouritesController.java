package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.ErrorResponseDto;
import com.example.SpringShop.Dto.ProductViewDto;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.UserNotFoundException;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/favourites")
public class FavouritesController {

    private final CustomerService customerService;
    private final ProductService productService;

    public FavouritesController(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping("/get-favourite-products")
    public ResponseEntity<?> getFavouriteProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            Customer customer = customerService.getCustomerByUsername(username);
            List<ProductViewDto> favouriteProducts = productService.getFavouriteProducts(customer);
            return ResponseEntity.ok(favouriteProducts);
        } catch (CustomerNotFoundException | UserNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving favorite products.");
        }
    }

    @PostMapping("/favorite-product")
    public ResponseEntity<?> makeProductFavourite(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Customer customer = customerService.getCustomerByUsername(username);
            productService.makeProductFavourite(customer, productId);
            return ResponseEntity.ok("Product added to favourites");
        } catch (CustomerNotFoundException | UserNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while making a product favourite.");
        }
    }

    @PostMapping("/unfavorite-product")
    public ResponseEntity<?> deleteFavouriteProduct(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Customer customer = customerService.getCustomerByUsername(username);
            productService.deleteFavouriteProduct(customer, productId);
            return ResponseEntity.ok("Product removed from favourites");
        } catch (CustomerNotFoundException | UserNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting a product from favourites.");
        }
    }

    //TODO: Implement the getFavouriteSearches, makeSearchFavourite, deleteFavouriteSearch endpoints

    @GetMapping("/get-recent-products")
    public ResponseEntity<?> getRecentProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Customer customer = customerService.getCustomerByUsername(username);
            var products = productService.getLast10ViewedProducts(customer.getId());
            return ResponseEntity.ok(products);

        } catch (CustomerNotFoundException | UserNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieveng last viewed products.");
        }
    }
}