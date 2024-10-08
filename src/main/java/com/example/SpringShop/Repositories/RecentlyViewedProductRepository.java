package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.RecentlyViewedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentlyViewedProductRepository extends JpaRepository<RecentlyViewedProduct, Long> {

    @Query("SELECT r FROM RecentlyViewedProduct r WHERE r.customer.id = :customerId ORDER BY r.viewedAt DESC")
    List<RecentlyViewedProduct> findTop10ByCustomerIdOrderByViewedAtDesc(Long customerId);
}