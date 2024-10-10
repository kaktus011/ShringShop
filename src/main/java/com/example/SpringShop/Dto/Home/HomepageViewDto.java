package com.example.SpringShop.Dto.Home;

import com.example.SpringShop.Dto.ProductViewDto;

import java.util.List;

public class HomepageViewDto {
    private List<String> categories;
    private List<ProductViewDto> lastViewedProducts;
    private List<String> lastSearched;
    private List<String> popularSearches;

    public HomepageViewDto(List<String> categories, List<ProductViewDto> lastViewedProducts, List<String> lastSearched, List<String> popularSearches) {
        this.categories = categories;
        this.lastViewedProducts = lastViewedProducts;
        this.lastSearched = lastSearched;
        this.popularSearches = popularSearches;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<ProductViewDto> getLastViewedProducts() {
        return lastViewedProducts;
    }

    public void setLastViewedProducts(List<ProductViewDto> lastViewedProducts) {
        this.lastViewedProducts = lastViewedProducts;
    }

    public List<String> getLastSearched() {
        return lastSearched;
    }

    public void setLastSearched(List<String> lastSearched) {
        this.lastSearched = lastSearched;
    }

    public List<String> getPopularSearches() {
        return popularSearches;
    }

    public void setPopularSearches(List<String> popularSearches) {
        this.popularSearches = popularSearches;
    }
}