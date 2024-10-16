package com.example.SpringShop.Exceptions;

public class ProductsWithCategoryExistsException extends RuntimeException {
    public ProductsWithCategoryExistsException(String category) {

      super("Cannot delete category: '" + category + "'.");
    }
}
