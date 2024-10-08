package com.example.SpringShop.Dto;

import java.util.List;

public class HomepageViewDto {
    private List<String> categories;
    private List<ProductViewDto> products;
    private List<String> lastSearched;
    private List<String> popularSearches;

    public HomepageViewDto(List<String> categories, List<ProductViewDto> products, List<String> lastSearched, List<String> popularSearches) {
        this.categories = categories;
        this.products = products;
        this.lastSearched = lastSearched;
        this.popularSearches = popularSearches;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<ProductViewDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductViewDto> products) {
        this.products = products;
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