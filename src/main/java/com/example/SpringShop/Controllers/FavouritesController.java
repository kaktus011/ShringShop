package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Dto.Product.ProductViewDto;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Exceptions.CannotAddToFavouritesException;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.InvalidProductException;
import com.example.SpringShop.Exceptions.UserNotFoundException;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @Secured({"CUSTOMER", "ADMIN"})
    @GetMapping("/get-favourites")
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

    @Secured("CUSTOMER")
    @PostMapping("/favourite/{id}")
    public ResponseEntity<?> makeProductFavourite(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Customer customer = customerService.getCustomerByUsername(username);
            productService.makeProductFavourite(customer, id);
            return ResponseEntity.ok("Product added to favourites!");
        } catch (CustomerNotFoundException | UserNotFoundException | InvalidProductException |
                 CannotAddToFavouritesException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while making a product favourite.");
        }
    }

    @Secured("CUSTOMER")
    @PostMapping("/unfavourite/{id}")
    public ResponseEntity<?> deleteFavouriteProduct(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Customer customer = customerService.getCustomerByUsername(username);
            productService.deleteFavouriteProduct(customer, id);
            return ResponseEntity.ok("Product removed from favourites!");
        } catch (CustomerNotFoundException | UserNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting a product from favourites.");
        }
    }

    //TODO: Implement the getFavouriteSearches, makeSearchFavourite, deleteFavouriteSearch endpoints

    @Secured("CUSTOMER")
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving last viewed products.");
        }
    }
}