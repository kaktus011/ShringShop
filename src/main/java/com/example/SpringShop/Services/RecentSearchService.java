package com.example.SpringShop.Services;

import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.RecentSearch;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecentSearchService {

    private final UserService userService;
    private final CustomerRepository customerRepository;

    @Autowired
    public RecentSearchService(UserService userService, CustomerRepository customerRepository) {
        this.userService = userService;
        this.customerRepository = customerRepository;
    }
    public List<String> getRecentSearches(String username){
        User user = userService.getUserByUsername(username);

        Customer customer = customerRepository.findByUser(user);
        return customer.getRecentSearches().stream()
                .map(RecentSearch::getSearchName)
                .collect(Collectors.toList());
    }

    public List<String> getTop10MostSearched() {
        return customerRepository.findTop10MostSearched();
    }
}
