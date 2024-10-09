package com.example.SpringShop.Entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_one_id", nullable = false)
    private Customer customerOne;

    @ManyToOne
    @JoinColumn(name = "customer_two_id", nullable = false)
    private Customer customerTwo;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Customer getCustomerOne() {return customerOne;}

    public void setCustomerOne(Customer customerOne) {this.customerOne = customerOne;}

    public Customer getCustomerTwo() {return customerTwo;}

    public void setCustomerTwo(Customer customerTwo) {this.customerTwo = customerTwo;}

    public List<Message> getMessages() {return messages;}

    public void setMessages(List<Message> messages) {this.messages = messages;}
}