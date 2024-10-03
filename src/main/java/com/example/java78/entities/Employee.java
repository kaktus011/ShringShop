package com.example.java78.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "employee_number")
    private Integer employeeNumber;

    public Long getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getFullName() { return firstName + " " + lastName; }
}
