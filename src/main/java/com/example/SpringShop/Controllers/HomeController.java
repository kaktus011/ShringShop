package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.HomepageViewDto;
import com.example.SpringShop.Dto.ProductViewDto;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.EntityMappers.ProductMapper;
import com.example.SpringShop.Services.CategoryService;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.RecentlyViewedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final RecentlyViewedProductService recentlyViewedProductService;

    @Autowired
    public HomeController(CategoryService categoryService, RecentlyViewedProductService recentlyViewedProductService, CustomerService customerService) {
        this.categoryService = categoryService;
        this.recentlyViewedProductService = recentlyViewedProductService;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<HomepageViewDto> loadHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Customer customer = customerService.getCustomerByUsername(currentUsername);

        var products = recentlyViewedProductService.getLast10ViewedProducts(customer.getId());
        List<ProductViewDto> productDtos = new ArrayList<>();
        for (var product : products) {
            var productDto = ProductMapper.toProductViewDto(product.getProduct());
            productDtos.add(productDto);
        }

        List<String> recentSearches = customerService.getRecentSearches(currentUsername);
        List<String> popularSearches = customerService.getTop10MostSearched();

        HomepageViewDto dto = new HomepageViewDto(categoryService.getAllCategoryNames(), productDtos, recentSearches, popularSearches);

        return ResponseEntity.ok(dto);
    }
}