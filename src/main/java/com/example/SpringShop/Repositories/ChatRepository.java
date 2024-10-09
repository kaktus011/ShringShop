package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findByCustomerOne_IdAndCustomerTwo_Id(Long customerOneId, Long customerTwoId);
}
