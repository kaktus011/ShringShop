package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUser_Username(String username);
    Customer findByUser(User user);
}

