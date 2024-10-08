package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.ProductViewDto;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.EntityMappers.ProductMapper;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.RecentlyViewedProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favourites")
public class FavouritesController {

    private final CustomerService customerService;
    private final RecentlyViewedProductService recentlyViewedProductService;

    public FavouritesController(CustomerService customerService, RecentlyViewedProductService recentlyViewedProductService) {
        this.customerService = customerService;
        this.recentlyViewedProductService = recentlyViewedProductService;
    }

    @GetMapping("/get-favourite-products")
    public ResponseEntity<List<ProductViewDto>> getFavouriteProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Customer customer = customerService.getCustomerByUsername(currentUsername);
        var favourites = customer.getFavouriteProducts().stream().map(ProductMapper::toProductViewDto).toList();

        return ResponseEntity.ok(favourites);
    }

    @PostMapping("/favorite-product")
    public ResponseEntity<String> makeProductFavourite(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Customer customer = customerService.getCustomerByUsername(currentUsername);

        customerService.makeProductFavourite(customer, productId);

        return ResponseEntity.ok("Product added to favourites");
    }

    @PostMapping("/unfavorite-product")
    public ResponseEntity<String> deleteFavouriteProduct(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Customer customer = customerService.getCustomerByUsername(currentUsername);

        customerService.deleteFavouriteProduct(customer, productId);

        return ResponseEntity.ok("Product removed from favourites");
    }

    //TODO: Implement the getFavouriteSearches, makeSearchFavourite, deleteFavouriteSearch endpoints

    @GetMapping("/get-recent-products")
    public ResponseEntity<List<ProductViewDto>> getRecentProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Customer customer = customerService.getCustomerByUsername(currentUsername);

        var products = recentlyViewedProductService.getLast10ViewedProducts(customer.getId());
        List<ProductViewDto> recentProductsDtos = products.stream()
                .map(product -> ProductMapper.toProductViewDto(product.getProduct()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(recentProductsDtos);
    }
}