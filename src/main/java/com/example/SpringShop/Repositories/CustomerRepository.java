package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUser_Username(String username);

    Customer findByUser(User user);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Customer c WHERE c.mobileNumber = :mobileNumber")
    boolean existsByMobileNumber(@Param("mobileNumber") String mobileNumber);

    @Query("SELECT c FROM Customer c WHERE c.mobileNumber = :mobileNumber")
    Customer findByMobileNumber(@Param("mobileNumber") String mobileNumber);

    @Query("SELECT rs.searchName FROM RecentSearch rs GROUP BY rs.searchName ORDER BY COUNT(rs.searchName) DESC")
    List<String> findTop10MostSearched();
}