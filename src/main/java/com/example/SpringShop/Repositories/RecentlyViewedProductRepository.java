package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.RecentlyViewedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentlyViewedProductRepository extends JpaRepository<RecentlyViewedProduct, Long> {

    @Query("SELECT rvp FROM RecentlyViewedProduct rvp WHERE rvp.customer.id = :customerId ORDER BY rvp.viewedAt DESC")
    List<RecentlyViewedProduct> findTop10ByCustomerIdOrderByViewedAtDesc(@Param("customerId") Long customerId);

    @Query("SELECT r FROM RecentlyViewedProduct r WHERE r.product.id = :productId AND r.customer = :customer")
    Optional<RecentlyViewedProduct> findByProductIdAndCustomer(@Param("productId") Long productId, @Param("customer") Customer customer);
}