package com.example.SpringShop.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "mobile_number", nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Column(name = "name", nullable = false, length = 70)
    private String name;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToMany
    @JoinTable(
            name = "customer_favourite_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> favouriteProducts;

    @ManyToMany
    @JoinTable(
            name = "customer_recent_searches",
            joinColumns =  @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "recent_search_id")
    )
    private List<RecentSearch> recentSearches;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<RecentlyViewedProduct> recentlyViewedProducts;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    public Long getId() {return id;}

    public User getUser() {return user;}

    public String getMobileNumber() {return mobileNumber;}

    public String getName() {return name;}

    public List<Order> getOrders() {return orders;}

    public List<Product> getFavouriteProducts() {return favouriteProducts;}

    public List<RecentSearch> getRecentSearches() {return recentSearches;}

    public List<RecentlyViewedProduct> getRecentlyViewedProducts() {return recentlyViewedProducts;}

    public List<Message> getMessages() {return messages;}

    public List<Product> getProducts() {return products;}

    public Cart getCart() {return cart;}

    public void setUser(User user) {this.user = user;}

    public void setMobileNumber(String mobileNumber) {this.mobileNumber = mobileNumber;}

    public void setName(String name) {this.name = name;}

    public void setOrders(List<Order> orders) {this.orders = orders;}

    public void setFavouriteProducts(List<Product> favouriteProducts) {this.favouriteProducts = favouriteProducts;}

    public void setRecentSearches(List<RecentSearch> recentSearches) {this.recentSearches = recentSearches;}

    public void setRecentlyViewedProducts(List<RecentlyViewedProduct> recentlyViewedProducts) {this.recentlyViewedProducts = recentlyViewedProducts;}

    public void setMessages(List<Message> messages) {this.messages = messages;}

    public void setProducts(List<Product> products) {this.products = products;}

    public void setCart(Cart cart) {this.cart = cart;}
}