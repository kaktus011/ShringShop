package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}