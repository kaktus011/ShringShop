package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.Customer.*;
import com.example.SpringShop.Dto.Customer.CustomerDetailsDto;
import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.EntityMappers.CustomerMapper;
import com.example.SpringShop.Exceptions.*;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Utilities.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    @Autowired
    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            String token = customerService.login(loginDto);
            return ResponseEntity.ok(token);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred with logging in. " + e.getMessage());
        }
    }

    @Secured("CUSTOMER")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || authorizationHeader.length() <= 7) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not logged in.");
        }

        String token = authorizationHeader.substring(7);
        jwtUtil.invalidateToken(token);

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully.");
    }

    @Secured({"CUSTOMER", "ADMIN"})
    @PutMapping("/change-username")
    public ResponseEntity<?> changeUsername(@Valid @RequestBody ChangeUsernameDto changeUsernameDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        try {
            Customer updatedCustomer = customerService.changeUsername(changeUsernameDto, currentUsername);
            var updatedCustomerDto = CustomerMapper.toCustomerDetailsDto(updatedCustomer);
            return ResponseEntity.ok(updatedCustomerDto);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (WrongUsernameException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (UsernameAlreadyTakenException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred with changing username. " + ex.getMessage());
        }
    }

    @Secured("CUSTOMER")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Customer updatedCustomer = customerService.changePassword(changePasswordDto, username);
            var updatedCustomerDto = CustomerMapper.toCustomerDetailsDto(updatedCustomer);
            return ResponseEntity.ok(updatedCustomerDto);
        } catch (InvalidPasswordException | PasswordMismatchException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred with changing password. " + ex.getMessage());
        }
    }

    @Secured({"CUSTOMER", "ADMIN"})
    @GetMapping("/details")
    public ResponseEntity<?> getCustomerDetails() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String currentName = authentication.getName();

         try{
             CustomerDetailsDto customerDetailsDto = customerService.getCustomerDetails(currentName);
             return ResponseEntity.ok(customerDetailsDto);
         }catch (UserNotFoundException | CustomerNotFoundException ex) {
             ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
         }catch (Exception ex) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("An unexpected error occurred with getting details. " + ex.getMessage());
         }
    }

    @Secured("CUSTOMER")
    @PutMapping("/change-mobile-number")
    public ResponseEntity<?> changeMobileNumber(@Valid @RequestBody ChangeMobileNumberDto changeMobileNumberDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Customer updatedCustomer = customerService.changeMobileNumber(changeMobileNumberDto, username);
            var updatedCustomerDto = CustomerMapper.toCustomerDetailsDto(updatedCustomer);
            return ResponseEntity.ok(updatedCustomerDto);
        } catch (InvalidMobileNumberException | MobileNumberAlreadyTakenException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NewNumberSameLikeOldNumberException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());}
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred with changing mobile number. " + ex.getMessage());
        }
    }

    @Secured("CUSTOMER")
    @PutMapping("/change-email")
    public ResponseEntity<?> changeEmail(@Valid @RequestBody ChangeEmailDto changeEmailDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();

        try {
            Customer updatedCustomer = customerService.changeEmail(changeEmailDto, currentName);
            var updatedCustomerDto = CustomerMapper.toCustomerDetailsDto(updatedCustomer);
            return ResponseEntity.ok(updatedCustomerDto);
        } catch (InvalidEmailException | EmailAlreadyTakenException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NewEmailSameLikeOldEmailException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred with changing email. " + ex.getMessage());
        }
    }
}
