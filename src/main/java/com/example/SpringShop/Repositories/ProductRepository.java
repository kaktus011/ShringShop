package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByTitleOrDescription(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Product p WHERE p.id = :productId AND p.customer = :customer")
    Product ProductWithCustomerExists(@Param("productId") Long productId, @Param("customer") Customer customer);
}
