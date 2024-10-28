package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.Customer.RegisterDto;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Exceptions.EmailAlreadyExistsException;
import com.example.SpringShop.Exceptions.MobileNumberAlreadyExistsException;
import com.example.SpringShop.Exceptions.UsernameAlreadyExistsException;
import com.example.SpringShop.Services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final CustomerService customerService;

    @Autowired
    public RegisterController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        try {
            Customer newCustomer = customerService.register(registerDto);
            return ResponseEntity.ok(newCustomer);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException | MobileNumberAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body("An unexpected error occurred with registering. " + ex.getMessage());
        }
    }
}
