package com.example.SpringShop.Services;

import com.example.SpringShop.Dto.Cart.CartViewDto;
import com.example.SpringShop.Dto.ProductInCartDto;
import com.example.SpringShop.Entities.Cart;
import com.example.SpringShop.Entities.Product;
import com.example.SpringShop.Exceptions.CartNotFoundException;
import com.example.SpringShop.Repositories.CartRepository;
import com.example.SpringShop.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public CartViewDto getCartForCustomer(Long customerId){
        Cart cart = getCartByCustomerId(customerId);
        List<ProductInCartDto> products = cart.getProducts().stream()
                .map(product -> new ProductInCartDto(
                        product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getImageUrl()))
                .collect(Collectors.toList());

        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCartId(cart.getId());
        cartViewDto.setProducts(products);
        cartViewDto.setCustomerId(customerId);
        cartViewDto.setTotalPrice(cart.getTotalPrice());
        return cartViewDto;
    }

    public CartViewDto addProductToCart(Long customerId, Long productId){
        Cart cart = getCartByCustomerId(customerId);
        Product productToAdd = productService.getProductById(productId);
        cart.getProducts().add(productToAdd);
        cart.setTotalPrice(cart.getProducts().stream().mapToDouble(Product::getPrice).sum());
         cartRepository.save(cart);

        List<ProductInCartDto> productsInCart = cart.getProducts().stream()
                .map(product -> new ProductInCartDto(
                        product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getImageUrl()))
                .collect(Collectors.toList());

        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCartId(cart.getId());
        cartViewDto.setProducts(productsInCart);
        cartViewDto.setCustomerId(customerId);
        cartViewDto.setTotalPrice(cart.getTotalPrice());
        return cartViewDto;
    }

    public CartViewDto deleteProductFromCart(Long customerId, Long productId){
        Cart cart = getCartByCustomerId(customerId);
        Product productToDelete = productService.getProductById(productId);
        cart.getProducts().remove(productToDelete);
        cart.setTotalPrice(cart.getProducts().stream().mapToDouble(Product::getPrice).sum());
        cartRepository.save(cart);

        List<ProductInCartDto> productsInCart = cart.getProducts().stream()
                .map(product -> new ProductInCartDto(
                        product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getImageUrl()))
                .collect(Collectors.toList());

        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCartId(cart.getId());
        cartViewDto.setProducts(productsInCart);
        cartViewDto.setCustomerId(customerId);
        cartViewDto.setTotalPrice(cart.getTotalPrice());
        return cartViewDto;
    }

    public CartViewDto clearcart(Long customerId){
        Cart cart = getCartByCustomerId(customerId);
        cart.setProducts(null);
        cart.setTotalPrice(null);
         cartRepository.save(cart);
        List<ProductInCartDto> productsInCart = cart.getProducts().stream()
                .map(product -> new ProductInCartDto(
                        product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getImageUrl()))
                .collect(Collectors.toList());

        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCartId(cart.getId());
        cartViewDto.setProducts(productsInCart);
        cartViewDto.setCustomerId(customerId);
        cartViewDto.setTotalPrice(cart.getTotalPrice());
        return cartViewDto;
    }

    public Cart getCartByCustomerId(Long customerId) {
        Cart cart = cartRepository.findByCustomer_Id(customerId);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cart;
    }

}
