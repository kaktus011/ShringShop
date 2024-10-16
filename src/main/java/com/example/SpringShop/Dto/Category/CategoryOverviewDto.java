package com.example.SpringShop.Dto.Category;

public class CategoryOverviewDto {
    public Long id;
    public String name;

    public CategoryOverviewDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {this.id = id;}

    public void setName(String name) {this.name = name;}

    public Long getId() { return id; }

    public String getName() { return name; }
}
