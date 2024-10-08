package com.example.SpringShop.Services;

import com.example.SpringShop.Entities.RecentlyViewedProduct;
import com.example.SpringShop.Repositories.RecentlyViewedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecentlyViewedProductService {

    private final RecentlyViewedProductRepository recentlyViewedProductRepository;

    @Autowired
    public RecentlyViewedProductService(RecentlyViewedProductRepository recentlyViewedProductRepository) {
        this.recentlyViewedProductRepository = recentlyViewedProductRepository;
    }

    public List<RecentlyViewedProduct> getLast10ViewedProducts(long customerId) {
        return recentlyViewedProductRepository.findTop10ByCustomerIdOrderByViewedAtDesc(customerId);
    }
}