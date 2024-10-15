package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.CustomerFavouriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerFavouriteProductRepository extends JpaRepository<CustomerFavouriteProduct, Long> {
    List<CustomerFavouriteProduct> findByCustomerId(Long customerId);
}
