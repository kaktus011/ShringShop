package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Dto.Product.ProductCreateDto;
import com.example.SpringShop.Dto.Product.ProductDetailsDto;
import com.example.SpringShop.Dto.Product.ProductViewDto;
import com.example.SpringShop.Entities.Product;
import com.example.SpringShop.EntityMappers.ProductMapper;
import com.example.SpringShop.Exceptions.*;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
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

        try{
            Long customerId = customerService.getCustomerId(username);
            Product product = productService.createProduct(customerId, productCreateDto);
            var productDto = ProductMapper.toProductIndexDto(product);
            return ResponseEntity.ok(productDto);
        }catch (UserNotFoundException | CustomerNotFoundException | CategoryNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred with creating a product.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> productDetails(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try{
            ProductDetailsDto productDetailsDto = productService.productDetails(id, username);
            return ResponseEntity.ok(productDetailsDto);
        }catch (UserNotFoundException | CustomerNotFoundException | InvalidProductException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred with retrieving product details.");
        }
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateProduct(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try{
            productService.deactivateProduct(id, username);
            return ResponseEntity.ok("Product marked as inactive successfully.");
        }catch (UserNotFoundException | CustomerNotFoundException | InvalidProductException |
                ProductWithCustomerNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred with deactivating a product.");
        }
    }

    @PutMapping("/deactivateFromAdmin/{id}")
    public ResponseEntity<?> deactivateProductFromAdmin(@PathVariable Long id) {
        try{
            productService.deactivateProductFromAdmin(id);
            return ResponseEntity.ok("Product marked as inactive successfully.");
        }catch (UserNotFoundException | CustomerNotFoundException | InvalidProductException |
                ProductWithCustomerNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred with deactivating a product.");
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductCreateDto productCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try{
            Product updatedProduct = productService.updateProduct(id, username, productCreateDto);
            var productDto = ProductMapper.toProductIndexDto(updatedProduct);
            return ResponseEntity.ok(productDto);
        }catch (UserNotFoundException | CustomerNotFoundException | InvalidProductException |
                ProductWithCustomerNotFoundException | CategoryNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred with updating a product.");
        }
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
