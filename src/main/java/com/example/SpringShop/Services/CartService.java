package com.example.SpringShop.Services;

import com.example.SpringShop.Dto.CartViewDto;
import com.example.SpringShop.Dto.ProductInCartDto;
import com.example.SpringShop.Entities.Cart;
import com.example.SpringShop.Entities.Product;
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

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartViewDto getCartForCustomer(Long customerId){
        Cart cart = cartRepository.findByCustomer_Id(customerId);

        if(cart == null){
            throw new RuntimeException("Cart Not Found");
        }

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

    public Cart addProductToCart(Long customerId, Long id){
        Cart cart = cartRepository.findByCustomer_Id(customerId);
        if(cart == null){
            throw new RuntimeException("Cart Not Found");
        }
        Product product = productRepository.findById(id).get();
        cart.getProducts().add(product);
        cart.setTotalPrice(cart.getProducts().stream().mapToDouble(Product::getPrice).sum());
        return cartRepository.save(cart);
    }

    public Cart deleteProductFromCart(Long customerId, Long id){
        Cart cart = cartRepository.findByCustomer_Id(customerId);
        if(cart == null){
            throw new RuntimeException("Cart Not Found");
        }
        Product product = productRepository.findById(id).get();
        cart.getProducts().remove(product);
        cart.setTotalPrice(cart.getProducts().stream().mapToDouble(Product::getPrice).sum());
        return cartRepository.save(cart);
    }

    public Cart clearcart(Long customerId){
        Cart cart = cartRepository.findByCustomer_Id(customerId);
        if(cart == null){
            throw new RuntimeException("Cart Not Found");
        }
        cart.setProducts(null);
        cart.setTotalPrice(null);
        return cartRepository.save(cart);
    }
}
