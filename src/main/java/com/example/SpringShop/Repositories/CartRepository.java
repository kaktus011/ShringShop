package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart findByCustomer_Id(Long customerId);
}
