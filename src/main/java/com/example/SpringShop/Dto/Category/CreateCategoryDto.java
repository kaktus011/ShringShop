package com.example.SpringShop.Dto.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryDto {
    @NotBlank(message = "Title is required.")
    @Size(max = 50, message = "Category must be up to 50 characters.")
    private String name;

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}
}
