package com.example.SpringShop.Services;

import com.example.SpringShop.Dto.ProductCreateDto;
import com.example.SpringShop.Dto.ProductDetailsDto;
import com.example.SpringShop.Dto.ProductViewDto;
import com.example.SpringShop.Entities.*;
import com.example.SpringShop.EntityMappers.ProductMapper;
import com.example.SpringShop.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RecentSearchRepository recentSearchRepository;

    @Autowired
    public ProductService(CustomerRepository customerRepository, ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository, RecentSearchRepository recentSearchRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.recentSearchRepository = recentSearchRepository;
    }

    public Product createProduct(Long customerId, ProductCreateDto productCreateDto){
        Product product = new Product();
        product.setTitle(productCreateDto.getTitle());
        product.setDescription(productCreateDto.getDescription());
        product.setLocation(productCreateDto.getLocation());
        product.setStatus(productCreateDto.getStatus());
        product.setImageUrl(productCreateDto.getImage());
        product.setPrice(productCreateDto.getPrice());
        product.setView(0);
        product.setActive(true);
        product.setCreationDate(LocalDateTime.now());

        Customer customer = customerRepository.findById(customerId).get();
        product.setCustomer(customer);
        Category category = categoryRepository.findByName(productCreateDto.getCategory());
        product.setCategory(category);
        productRepository.save(product);
        return product;
    }

    public ProductDetailsDto productDetails(Long id, String username){
        Product product = productRepository.findById(id).get();
        ProductDetailsDto productDetailsDto = new ProductDetailsDto();
        productDetailsDto.setId(product.getId());
        productDetailsDto.setTitle(product.getTitle());
        productDetailsDto.setDescription(product.getDescription());
        productDetailsDto.setPrice(product.getPrice());
        productDetailsDto.setLocation(product.getLocation());
        productDetailsDto.setCategory(product.getCategory().getName());
        productDetailsDto.setImage(product.getImageUrl());
        productDetailsDto.setStatus(product.getStatus());
        productDetailsDto.setCreatorId(product.getCustomer().getId());
        productDetailsDto.setCreatorName(product.getCustomer().getName());
        productDetailsDto.setCreatorPhone(product.getCustomer().getMobileNumber());
        User user = userRepository.findByUsername(username);
        productDetailsDto.setCreatorEmail(product.getCustomer().getUser().getEmail());

        RecentlyViewedProduct recentlyViewedProduct = new RecentlyViewedProduct();
        recentlyViewedProduct.setProduct(product);
        recentlyViewedProduct.setViewedAt(LocalDateTime.now());
        recentlyViewedProduct.setCustomer(product.getCustomer());
        return productDetailsDto;
    }

    public void deactivateProduct(Long id){
        Optional<Product> product = productRepository.findById(id);
        product.get().setActive(false);
        productRepository.save(product.get());

    }
    public Product updateProduct(Long productId, Long customerId, ProductCreateDto productCreateDto) {
        Product product = productRepository.findById(productId).get();

        product.setTitle(productCreateDto.getTitle());
        product.setDescription(productCreateDto.getDescription());
        product.setPrice(productCreateDto.getPrice());
        product.setLocation(productCreateDto.getLocation());
        Category category = categoryRepository.findByName(productCreateDto.getCategory());
        product.setCategory(category);
        product.setImageUrl(productCreateDto.getImage());
        product.setStatus(productCreateDto.getStatus());

        productRepository.save(product);
        return product;
    }
    public Page<ProductViewDto> getFilteredProducts(
            String username,
            String categoryName,
            Double minPrice,
            Double maxPrice,
            String location,
            String searchTerm,
            int page,
            int size) {

        // Fetch all products from the database
        List<Product> allProducts = productRepository.findAll();

        // Apply filters
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> (categoryName == null || product.getCategory().getName().equalsIgnoreCase(categoryName)) &&
                        (minPrice == null || product.getPrice() >= minPrice) &&
                        (maxPrice == null || product.getPrice() <= maxPrice) &&
                        (location == null || product.getLocation().toLowerCase().contains(location.toLowerCase())) &&
                        (searchTerm == null ||
                                product.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                product.getDescription().toLowerCase().contains(searchTerm.toLowerCase())))
                .collect(Collectors.toList());


        List<ProductViewDto> productViewDtos = filteredProducts.stream()
                .map(ProductMapper::toProductViewDto)
                .collect(Collectors.toList());

        User user = userRepository.findByUsername(username);
        Customer customer = customerRepository.findByUser(user);
        if (searchTerm != null) {
            RecentSearch recentSearch = new RecentSearch();
            recentSearch.setCustomer(customer);
            recentSearch.setSearchDate(LocalDateTime.now());
            recentSearch.setSearchName(searchTerm);
            recentSearchRepository.save(recentSearch);
        }
        int start = Math.toIntExact(PageRequest.of(page, size).getOffset());
        int end = Math.min((start + size), productViewDtos.size());

        Page<ProductViewDto> productPage = new PageImpl<>(productViewDtos.subList(start, end), PageRequest.of(page, size), productViewDtos.size());

        return productPage;
    }

    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
