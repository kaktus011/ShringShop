package com.example.SpringShop.EntityMappers;

import com.example.SpringShop.Dto.Customer.CustomerDetailsDto;
import com.example.SpringShop.Entities.Customer;

public class CustomerMapper {

    public static CustomerDetailsDto toCustomerDetailsDto(Customer customer) {
        var dto = new CustomerDetailsDto();
        dto.setUsername(customer.getUser().getUsername());
        dto.setEmail(customer.getUser().getEmail());
        dto.setName(customer.getName());
        dto.setMobileNumber(customer.getMobileNumber());
        dto.setPassword(customer.getUser().getPassword());
        dto.setId(customer.getId());
        return dto;
    }
}