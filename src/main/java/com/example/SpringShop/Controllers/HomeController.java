package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Dto.Home.HomepageViewDto;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.UserNotFoundException;
import com.example.SpringShop.Services.CategoryService;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import com.example.SpringShop.Services.RecentSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final RecentSearchService recentSearchService;

    @Autowired
    public HomeController(CategoryService categoryService, ProductService productService, CustomerService customerService, RecentSearchService recentSearchService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.customerService = customerService;
        this.recentSearchService = recentSearchService;
    }

    //TODO
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<?> loadHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            Long customerId = customerService.getCustomerId(username);

            var products = productService.getLast10ViewedProducts(customerId);
            List<String> recentSearches = recentSearchService.getRecentSearches(username);
            List<String> popularSearches = recentSearchService.getTop10MostSearched();

            HomepageViewDto dto = new HomepageViewDto(categoryService.getAllCategoryNames(), products, recentSearches, popularSearches);
            return ResponseEntity.ok(dto);
        } catch (CustomerNotFoundException | UserNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + errorResponse);
        }
    }
}