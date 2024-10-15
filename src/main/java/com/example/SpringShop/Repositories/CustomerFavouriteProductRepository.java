package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.CustomerFavouriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerFavouriteProductRepository extends JpaRepository<CustomerFavouriteProduct, Long> {
    List<CustomerFavouriteProduct> findByCustomerId(Long customerId);

    @Query("SELECT cfp FROM CustomerFavouriteProduct cfp WHERE cfp.customer.id = :customerId AND cfp.product.id = :productId")
    CustomerFavouriteProduct findByCustomerIdAndByProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);
}
