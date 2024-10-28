package com.example.SpringShop;

import com.example.SpringShop.Controllers.RegisterController;
import com.example.SpringShop.Dto.Customer.RegisterDto;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Exceptions.EmailAlreadyExistsException;
import com.example.SpringShop.Exceptions.MobileNumberAlreadyExistsException;
import com.example.SpringShop.Exceptions.UsernameAlreadyExistsException;
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

public class RegisterControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testUser");
        registerDto.setEmail("test@example.com");
        registerDto.setMobileNumber("1234567890");
        registerDto.setPassword("password");

        Customer customer = new Customer();
        customer.setName("testUser");

        when(customerService.register(registerDto)).thenReturn(customer);

        ResponseEntity<?> response = registerController.register(registerDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testUser");

        when(customerService.register(registerDto)).thenThrow(new UsernameAlreadyExistsException());

        ResponseEntity<?> response = registerController.register(registerDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Username already exists.", response.getBody());
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");

        when(customerService.register(registerDto)).thenThrow(new EmailAlreadyExistsException("test@example.com"));

        ResponseEntity<?> response = registerController.register(registerDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exists.", response.getBody());
    }

    @Test
    void testRegister_MobileNumberAlreadyExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setMobileNumber("1234567890");

        when(customerService.register(registerDto)).thenThrow(new MobileNumberAlreadyExistsException("1234567890"));

        ResponseEntity<?> response = registerController.register(registerDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Mobile number already exists.", response.getBody());
    }

    @Test
    void testRegister_RuntimeException() {
        RegisterDto registerDto = new RegisterDto();

        when(customerService.register(registerDto)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = registerController.register(registerDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("An unexpected error occurred with registering. Unexpected error", response.getBody());
    }
}