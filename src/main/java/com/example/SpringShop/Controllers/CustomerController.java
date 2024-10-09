package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.*;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        Customer newCustomer = customerService.register(registerDto);
        return ResponseEntity.ok(newCustomer);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            // Attempt to log in the user using the service
            String token = customerService.login(loginDto);
            return ResponseEntity.ok(token); // Return the JWT token
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PutMapping("/change-username")
    public ResponseEntity<?> changeUsername(@Valid @RequestBody ChangeUsernameDto changeUsernameDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Customer updatedCustomer = customerService.changeUsername(changeUsernameDto, currentUsername);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();
        Customer updatedCustomer = customerService.changePassword(changePasswordDto, currentName);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/details")
    public ResponseEntity<CustomerDetailsDto> getCustomerDetails() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String currentName = authentication.getName();
         CustomerDetailsDto customerDetailsDto = customerService.getCustomerDetails(currentName);
         return ResponseEntity.ok(customerDetailsDto);
    }

    @PutMapping("/change-mobileNumber")
    public ResponseEntity<?> changeMobileNumber(@Valid @RequestBody ChangeMobileNumberDto changeMobileNumberDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();
        Customer customer = customerService.changeMobileNumber(changeMobileNumberDto, currentName);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/change-email")
    public ResponseEntity<?> changeEmail(@Valid @RequestBody ChangeEmailDto changeEmailDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();
        Customer customer = customerService.changeEmail(changeEmailDto, currentName);
        return ResponseEntity.ok(customer);
    }
}
