package com.example.SpringShop.EntityMappers;

import com.example.SpringShop.Dto.Cart.CartViewDto;
import com.example.SpringShop.Dto.Product.ProductInCartDto;
import com.example.SpringShop.Entities.Cart;
import com.example.SpringShop.Exceptions.CartNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {


    public static CartViewDto toCartViewDto(Cart cart, Long customerId) {
        if (cart == null) {
            throw new CartNotFoundException();
        }

        List<ProductInCartDto> products = cart.getProducts() != null
                ? cart.getProducts().stream()
                .map(product -> new ProductInCartDto(
                        product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getImageUrl()))
                .collect(Collectors.toList())
                : List.of();

        CartViewDto cartViewDto = new CartViewDto();
        cartViewDto.setCartId(cart.getId());
        cartViewDto.setProducts(products);
        cartViewDto.setCustomerId(customerId);
        cartViewDto.setTotalPrice(cart.getTotalPrice() != null ? cart.getTotalPrice() : 0.0);
        cartViewDto.setTotalProducts(cart.getProducts().size() > 0 ? cart.getProducts().size() : 0);

        return cartViewDto;
    }
}
