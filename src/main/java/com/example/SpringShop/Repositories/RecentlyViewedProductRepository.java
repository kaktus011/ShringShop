package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.RecentlyViewedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentlyViewedProductRepository extends JpaRepository<RecentlyViewedProduct, Long> {

    @Query("SELECT rvp FROM RecentlyViewedProduct rvp WHERE rvp.customer.id = :customerId ORDER BY rvp.viewedAt DESC")
    List<RecentlyViewedProduct> findTop10ByCustomerIdOrderByViewedAtDesc(@Param("customerId") Long customerId);
}