package com.example.SpringShop.Services;

import com.example.SpringShop.Dto.*;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.RecentSearch;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.Repositories.CustomerRepository;
import com.example.SpringShop.Repositories.UserRepository;
import com.example.SpringShop.Utilities.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JWTUtil jwtUtil,
                           AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public Customer register(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setRole("CUSTOMER");

        userRepository.save(user);

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setMobileNumber(registerDto.getMobileNumber());
        customer.setName(registerDto.getName());

        return customerRepository.save(customer);
    }
    public String login(LoginDto loginDto){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
            return jwtUtil.generateToken(userDetails.getUsername());
        }
        catch(Exception e){
            throw new RuntimeException("Invalid username or password");
        }
    }
    public Customer changeUsername(ChangeUsernameDto changeUsernameDto, String currentUsername){
        User user = userRepository.findByUsername(currentUsername);

        if (user == null){
            throw new RuntimeException("User not found");
        }

        if (!changeUsernameDto.getOldUsername().equals(user.getUsername())){
            throw new RuntimeException("Wrong username for user");
        }

        User existingUser = userRepository.findByUsername(changeUsernameDto.getNewUsername());
        if (existingUser != null){
            throw new RuntimeException("Username is already taken");
        }
        user.setUsername(changeUsernameDto.getNewUsername());
        userRepository.save(user);
        return customerRepository.findByUser(user);
    }
    public Customer changePassword(ChangePasswordDto changePasswordDto, String currentPassword){
        User user = userRepository.findByUsername(currentPassword);

        if (user == null){
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(),user.getPassword())){
            throw new RuntimeException("Wrong password");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        return customerRepository.findByUser(user);
    }
    public CustomerDetailsDto getCustomerDetails(String currentName){
        User user = userRepository.findByUsername(currentName);
        if (user == null){
            throw new RuntimeException("User not found");
        }
        CustomerDetailsDto customerDetailsDto = new CustomerDetailsDto();
        customerDetailsDto.setUsername(user.getUsername());
        customerDetailsDto.setEmail(user.getEmail());
        Customer customer = customerRepository.findByUser(user);
        customerDetailsDto.setName(customer.getName());
        customerDetailsDto.setMobileNumber(customer.getMobileNumber());
        return customerDetailsDto;
    }
    public Customer changeMobileNumber(ChangeMobileNumberDto changeMobileNumberDto, String currentMobileNumber){
        User user = userRepository.findByUsername(currentMobileNumber);
        if (user == null){
            throw new RuntimeException("User not found");
        }
        Customer customer = customerRepository.findByUser(user);
        if (!changeMobileNumberDto.getOldMobileNumber().equals(customer.getMobileNumber())){
            throw new RuntimeException("Mobiles number should be different.");
        }
        customer.setMobileNumber(changeMobileNumberDto.getNewMobileNumber());
        customerRepository.save(customer);
        return customer;
    }

    public Customer changeEmail(ChangeEmailDto changeEmailDto, String currentEmail){
        User user = userRepository.findByUsername(currentEmail);
        if (user == null){
            throw new RuntimeException("User not found");
        }
        if (!changeEmailDto.getOldEmail().equals(user.getEmail())){
            throw new RuntimeException("Emails should be different.");
        }
        user.setEmail(changeEmailDto.getNewEmail());
        userRepository.save(user);
        return customerRepository.findByUser(user);
    }

    public Customer getCustomerByUsername(String username){
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new RuntimeException("User not found");
        }
        return customerRepository.findByUser(user);
    }

    public List<String> getRecentSearches(String username){
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new RuntimeException("User not found");
        }

        Customer customer = customerRepository.findByUser(user);
        return customer.getRecentSearches().stream()
                .map(RecentSearch::getSearchName)
                .collect(Collectors.toList());
    }

    public List<String> getTop10MostSearched() {
        return customerRepository.findTop10MostSearched();
    }
}