package com.example.SpringShop.Services;

import com.example.SpringShop.Constants.UserRoleConstants;
import com.example.SpringShop.Dto.Customer.*;
import com.example.SpringShop.Dto.Customer.CustomerDetailsDto;
import com.example.SpringShop.Entities.Cart;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.Role;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.EntityMappers.CustomerMapper;
import com.example.SpringShop.Exceptions.*;
import com.example.SpringShop.Repositories.CartRepository;
import com.example.SpringShop.Repositories.CustomerRepository;
import com.example.SpringShop.Repositories.RoleRepository;
import com.example.SpringShop.Repositories.UserRepository;
import com.example.SpringShop.Utilities.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final CartRepository cartRepository;
    private final UserService userService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JWTUtil jwtUtil,
                           AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService, CartRepository cartRepository, UserService userService) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    public Customer register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new EmailAlreadyExistsException(registerDto.getEmail());
        }

        if (customerRepository.existsByMobileNumber(registerDto.getMobileNumber())) {
            throw new MobileNumberAlreadyExistsException(registerDto.getMobileNumber());
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());

        Role customerRole = roleRepository.findByName(UserRoleConstants.CUSTOMER);
        user.setRoles(Set.of(customerRole));
        userRepository.save(user);

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setMobileNumber(registerDto.getMobileNumber());
        customer.setName(registerDto.getName());
        customerRepository.save(customer);

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cartRepository.save(cart);

        return customer;
    }

    public String login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
            User user = userService.getUserByUsername(userDetails.getUsername());
            return jwtUtil.generateToken(userDetails.getUsername(), user);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
    }

    public Customer changeUsername(ChangeUsernameDto changeUsernameDto, String currentUsername) {
        User user = userService.getUserByUsername(currentUsername);

        if (!changeUsernameDto.getOldUsername().equals(user.getUsername())) {
            throw new WrongUsernameException();
        }

        User existingUser = userRepository.findByUsername(changeUsernameDto.getNewUsername());
        if (existingUser != null) {
            throw new UsernameAlreadyTakenException(changeUsernameDto.getNewUsername());
        }

        user.setUsername(changeUsernameDto.getNewUsername());
        userRepository.save(user);
        return customerRepository.findByUser(user);
    }

    public Customer changePassword(ChangePasswordDto changePasswordDto, String username) {
        User user = userService.getUserByUsername(username);

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (passwordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())) {
            throw new PasswordMismatchException();
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        return customerRepository.findByUser(user);
    }

    public CustomerDetailsDto getCustomerDetails(String username) {
        Customer customer = getCustomerByUsername(username);

        return CustomerMapper.toCustomerDetailsDto(customer);
    }

    public Customer changeMobileNumber(ChangeMobileNumberDto changeMobileNumberDto, String username) {
        User user = userService.getUserByUsername(username);
        Customer customer = customerRepository.findByUser(user);
        if (!changeMobileNumberDto.getOldMobileNumber().equals(customer.getMobileNumber())) {
            throw new InvalidMobileNumberException();
        }

        if (changeMobileNumberDto.getOldMobileNumber().equals(changeMobileNumberDto.getNewMobileNumber())) {
            throw new NewNumberSameLikeOldNumberException();
        }

        Customer existingCustomer = customerRepository.findByMobileNumber(changeMobileNumberDto.getNewMobileNumber());
        if (existingCustomer != null) {
            throw new MobileNumberAlreadyTakenException(changeMobileNumberDto.getNewMobileNumber());
        }

        customer.setMobileNumber(changeMobileNumberDto.getNewMobileNumber());
        customerRepository.save(customer);
        return customer;
    }

    public Customer changeEmail(ChangeEmailDto changeEmailDto, String username) {
        User user = userService.getUserByUsername(username);

        if (!changeEmailDto.getOldEmail().equals(user.getEmail())) {
            throw new InvalidEmailException();
        }

        if (changeEmailDto.getOldEmail().equals(changeEmailDto.getNewEmail())) {
            throw new NewEmailSameLikeOldEmailException();
        }

        User existingUser = userRepository.findByEmail(changeEmailDto.getNewEmail());
        if (existingUser != null) {
            throw new EmailAlreadyTakenException(changeEmailDto.getNewEmail());
        }

        user.setEmail(changeEmailDto.getNewEmail());
        userRepository.save(user);
        return customerRepository.findByUser(user);
    }

    public Customer getCustomerByUsername(String username) {
        User user = userService.getUserByUsername(username);

        return customerRepository.findByUser(user);
    }

    public Long getCustomerId(String username) {
        User user = userService.getUserByUsername(username);
        Customer customer = customerRepository.findByUser(user);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        return customer.getId();
    }
}