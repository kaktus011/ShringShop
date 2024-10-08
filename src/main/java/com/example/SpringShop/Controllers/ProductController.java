package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.ProductCreateDto;
import com.example.SpringShop.Dto.ProductDetailsDto;
import com.example.SpringShop.Dto.ProductViewDto;
import com.example.SpringShop.Entities.Product;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final CustomerService customerService;
    private final ProductService productService;

    public ProductController(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateDto productCreateDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long customerId = customerService.getCustomerId(username);

        Product product = productService.createProduct(customerId, productCreateDto);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsDto> productDetails(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ProductDetailsDto productDetailsDto = productService.productDetails(id, username);
        return ResponseEntity.ok(productDetailsDto);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.ok("Product marked as inactive successfully.");
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductCreateDto productCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long customerId = customerService.getCustomerId(username);

        Product updatedProduct = productService.updateProduct(id, customerId, productCreateDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductViewDto>> getFilteredProducts(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Page<ProductViewDto> products = productService.getFilteredProducts(username, categoryName, minPrice, maxPrice, location, searchTerm, page, size);
        return ResponseEntity.ok(products);
    }

}
