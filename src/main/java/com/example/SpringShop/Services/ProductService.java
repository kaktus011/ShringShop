package com.example.SpringShop.Services;

import com.example.SpringShop.Dto.Product.ProductCreateDto;
import com.example.SpringShop.Dto.Product.ProductDetailsDto;
import com.example.SpringShop.Dto.Product.ProductViewDto;
import com.example.SpringShop.Entities.*;
import com.example.SpringShop.EntityMappers.ProductMapper;
import com.example.SpringShop.Exceptions.*;
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
    private final RecentlyViewedProductRepository recentlyViewedProductRepository;
    private final CustomerService customerService;
    private final CustomerFavouriteProductRepository customerFavouriteProductRepository;

    @Autowired
    public ProductService(CustomerRepository customerRepository, ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository, RecentSearchRepository recentSearchRepository, RecentlyViewedProductRepository recentlyViewedProductRepository, CustomerService customerService, CustomerFavouriteProductRepository customerFavouriteProductRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.recentSearchRepository = recentSearchRepository;
        this.recentlyViewedProductRepository = recentlyViewedProductRepository;
        this.customerService = customerService;
        this.customerFavouriteProductRepository = customerFavouriteProductRepository;
    }

    public Product createProduct(Long customerId, ProductCreateDto productCreateDto){
        Category category = categoryRepository.findByName(productCreateDto.getCategory());
        if(category == null){
            throw new CategoryNotFoundException(productCreateDto.getCategory());
        }
        Customer customer = customerRepository.findById(customerId).get();
        if(customer == null){
            throw new CustomerNotFoundException();
        }

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

        product.setCustomer(customer);
        product.setCategory(category);
        productRepository.save(product);
        return product;
    }

    public ProductDetailsDto productDetails(Long productId, String username) {
        Product product = getProductById(productId);
        Customer customer = customerService.getCustomerByUsername(username);

        Optional<RecentlyViewedProduct> recentlyViewed = recentlyViewedProductRepository.findByProductIdAndCustomer(productId, customer);

        RecentlyViewedProduct recentlyViewedProduct;
        if (recentlyViewed.isPresent()) {
            recentlyViewedProduct = recentlyViewed.get();
            recentlyViewedProduct.setViewedAt(LocalDateTime.now());
        } else {
            recentlyViewedProduct = new RecentlyViewedProduct();
            recentlyViewedProduct.setProduct(product);
            recentlyViewedProduct.setCustomer(customer);
            recentlyViewedProduct.setViewedAt(LocalDateTime.now());
            product.setView(product.getView() + 1);
            productRepository.save(product);
        }

        recentlyViewedProductRepository.save(recentlyViewedProduct);

        ProductDetailsDto productDetailsDto = new ProductDetailsDto(product.getId(), product.getTitle(), product.getDescription(), product.getPrice(), product.getCategory().getId(), product.getCategory().getName(), product.getImageUrl(), product.getStatus(), product.getLocation(), product.getView() , product.getCustomer().getId(), product.getCustomer().getName(), product.getCustomer().getMobileNumber(), product.getCustomer().getUser().getEmail(), product.getCustomersWhoFavourited().size());


        return productDetailsDto;
    }

    public void deactivateProduct(Long id, String username){
        Customer customer = customerService.getCustomerByUsername(username);
        Product product = getProductById(id);
        if(productRepository.ProductWithCustomerExists(id, customer) == null){
            throw new ProductWithCustomerNotFoundException();
        }
        product.setActive(false);
        productRepository.save(product);
    }

    public void deactivateProductFromAdmin(Long productId){
        Product product = getProductById(productId);
        if(productRepository.ProductWithCustomerExists(productId, product.getCustomer()) == null){
            throw new ProductWithCustomerNotFoundException();
        }
        Customer customer = customerService.getCustomerByUsername(product.getCustomer().getUser().getUsername());
        product.setActive(false);
        productRepository.save(product);
    }

    public Product updateProduct(Long productId, String username, ProductCreateDto productCreateDto) {
        Product product = getProductById(productId);
        Customer customer = customerService.getCustomerByUsername(username);
        if(productRepository.ProductWithCustomerExists(productId, customer) == null){
            throw new ProductWithCustomerNotFoundException();
        }
        Category category = categoryRepository.findByName(productCreateDto.getCategory());
        if(category == null){
            throw new CategoryNotFoundException(productCreateDto.getCategory());
        }

        product.setTitle(productCreateDto.getTitle());
        product.setDescription(productCreateDto.getDescription());
        product.setPrice(productCreateDto.getPrice());
        product.setLocation(productCreateDto.getLocation());
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

    public List<ProductViewDto> getLast10ViewedProducts(Long customerId) {
        List<RecentlyViewedProduct> products = recentlyViewedProductRepository.findTop10ByCustomerIdOrderByViewedAtDesc(customerId);

        return products.stream()
                .map(product -> ProductMapper.toProductViewDto(product.getProduct()))
                .collect(Collectors.toList());
    }

    public List<ProductViewDto> getFavouriteProducts(Customer customer) {
        List<CustomerFavouriteProduct> favouriteProducts = customerFavouriteProductRepository.findByCustomerId(customer.getId());

        return favouriteProducts.stream()
                .map(favourite -> ProductMapper.toProductViewDto(favourite.getProduct()))
                .collect(Collectors.toList());
    }

    public void makeProductFavourite(Customer customer, Long productId) {
        Product product = getProductById(productId);

        if (product.getCustomer().equals(customer)) {
            throw new CannotAddToFavouritesException("Cannot favorite your own product.");
        }

        if (customer.getFavouriteProducts().stream()
                .noneMatch(fav -> fav.getProduct().equals(product))) {

            CustomerFavouriteProduct favourite = new CustomerFavouriteProduct();
            favourite.setCustomer(customer);
            favourite.setProduct(product);

            customer.getFavouriteProducts().add(favourite);
            product.getCustomersWhoFavourited().add(favourite);
            customerRepository.save(customer);
        }
    }

    public void deleteFavouriteProduct(Customer customer, Long productId) {
        Product product = getProductById(productId);
        Optional<CustomerFavouriteProduct> favouriteOpt = customerFavouriteProductRepository
                .findByCustomerIdAndByProductId(customer.getId(), productId);

        CustomerFavouriteProduct favourite = favouriteOpt
                .orElseThrow(() -> new ProductNotInFavouritesException("The product is not in your favourites."));

        customer.getFavouriteProducts().remove(favourite);
        product.getCustomersWhoFavourited().remove(favourite);

        customerFavouriteProductRepository.delete(favourite);
        productRepository.save(product);
        customerRepository.save(customer);
    }

    public Product getProductById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        Product product = productOpt.orElseThrow(() -> new InvalidProductException());
        return product;
    }
}
