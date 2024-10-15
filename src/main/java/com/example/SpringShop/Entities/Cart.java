package com.example.SpringShop.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products =  new ArrayList<>();

    @Column(name = "total_price")
    private Double totalPrice;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Customer getCustomer() {return customer;}

    public void setCustomer(Customer customer) {this.customer = customer;}

    public List<Product> getProducts() {return products;}

    public void setProducts(List<Product> products) {this.products = products;}

    public Double getTotalPrice() {return totalPrice;}

    public void setTotalPrice(Double totalPrice) {this.totalPrice = totalPrice;}
}
