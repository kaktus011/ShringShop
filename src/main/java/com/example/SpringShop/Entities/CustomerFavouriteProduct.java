package com.example.SpringShop.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_favourite_product")
public class CustomerFavouriteProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Customer getCustomer() {return customer;}

    public void setCustomer(Customer customer) {this.customer = customer;}

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}
}
