package com.example.SpringShop;

import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.RecentSearch;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.Exceptions.UserNotFoundException;
import com.example.SpringShop.Repositories.CustomerRepository;
import com.example.SpringShop.Services.RecentSearchService;
import com.example.SpringShop.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecentSearchServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RecentSearchService recentSearchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRecentSearches_Success() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        Customer customer = new Customer();
        RecentSearch search1 = new RecentSearch();
        search1.setSearchName("search1");
        RecentSearch search2 = new RecentSearch();
        search2.setSearchName("search2");
        customer.setRecentSearches(Arrays.asList(search1, search2));

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(customerRepository.findByUser(user)).thenReturn(customer);

        List<String> recentSearches = recentSearchService.getRecentSearches(username);

        assertNotNull(recentSearches);
        assertEquals(2, recentSearches.size());
        assertTrue(recentSearches.contains("search1"));
        assertTrue(recentSearches.contains("search2"));
    }

    @Test
    public void testGetRecentSearches_UserNotFound() {
        String username = "nonExistentUser";

        when(userService.getUserByUsername(username)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> recentSearchService.getRecentSearches(username));
    }

    @Test
    public void testGetTop10MostSearched_Success() {
        List<String> topSearches = Arrays.asList("search1", "search2", "search3");

        when(customerRepository.findTop10MostSearched()).thenReturn(topSearches);

        List<String> result = recentSearchService.getTop10MostSearched();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("search1"));
        assertTrue(result.contains("search2"));
        assertTrue(result.contains("search3"));
    }
}