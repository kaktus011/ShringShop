package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE (c.customerOne.id = :customerOneId AND c.customerTwo.id = :customerTwoId) " +
            "OR (c.customerOne.id = :customerTwoId AND c.customerTwo.id = :customerOneId)")
    Chat findChatBetweenCustomers(@Param("customerOneId") Long customerOneId, @Param("customerTwoId") Long customerTwoId);

    @Query("SELECT c FROM Chat c WHERE c.customerOne.id = :customerId OR c.customerTwo.id = :customerId")
    List<Chat> findAllChatsForCustomer(@Param("customerId") Long customerId);
}
