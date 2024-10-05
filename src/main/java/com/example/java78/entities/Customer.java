package com.example.java78.entities;

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
    private User user; //sochi kum entityto na user, zashtoto vseki customer s rolq customer e user

    @Column(name = "mobile_number", nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Column(name = "name", nullable = false, length = 70)
    private String name; //ime, koeto moje da e lichno ime ili ime na firmata na choveka, toest da se registrirame pod imeto na firmata si

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders; //vsichkite poruchki koito ima chovek

    @ManyToMany
    @JoinTable(
            name = "customer_favourite_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> favouriteProducts; //suzdava se tazlica many-to-many customer_favourite_products, koqto ima klychove kum id-to na rodukta i id-to na choveka

    @ManyToMany
    @JoinTable(
            name = "customer_recent_searches",
            joinColumns =  @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "recent_search_id")
    )
    private List<RecentSearch> recentSearches; //many to many tablica customer_recent_searches, koqto sochi kum id-to na customer i id-to na recent seracha

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> messages; //suobshteniqta, koito choveka ima, kato se svurzva s tablicata message po sender

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Product> products; //produktite , koito e uploadnal choveka

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;
}
