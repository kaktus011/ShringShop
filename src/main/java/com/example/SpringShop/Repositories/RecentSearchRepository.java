package com.example.SpringShop.Repositories;

import com.example.SpringShop.Entities.RecentSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {
}
