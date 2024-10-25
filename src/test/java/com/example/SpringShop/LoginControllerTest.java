package com.example.SpringShop;

import com.example.SpringShop.Controllers.LoginController;
import com.example.SpringShop.Dto.Customer.LoginDto;
import com.example.SpringShop.Exceptions.InvalidCredentialsException;
import com.example.SpringShop.Services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("password");

        String token = "mockToken";

        when(customerService.login(loginDto)).thenReturn(token);

        ResponseEntity<?> response = loginController.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody());
    }

    @Test
    void testLogin_InvalidCredentials() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("wrongPassword");

        when(customerService.login(loginDto)).thenThrow(new InvalidCredentialsException());

        ResponseEntity<?> response = loginController.login(loginDto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void testLogin_RuntimeException() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("password");

        when(customerService.login(loginDto)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = loginController.login(loginDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred with logging in. Unexpected error", response.getBody());
    }
}