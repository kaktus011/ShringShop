package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUser_Username(String username);
    Customer findByUser(User user);

    @Query("SELECT rs.searchName FROM RecentSearch rs GROUP BY rs.searchName ORDER BY COUNT(rs.searchName) DESC")
    List<String> findTop10MostSearched();
}