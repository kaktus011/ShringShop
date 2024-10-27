package com.example.SpringShop;

import com.example.SpringShop.Constants.UserRoleConstants;
import com.example.SpringShop.Entities.Role;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.Repositories.RoleRepository;
import com.example.SpringShop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpringShopApplication {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public SpringShopApplication(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            if (roleRepository.findAll().isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName(UserRoleConstants.ADMIN);
                roleRepository.save(adminRole);

                Role customerRole = new Role();
                customerRole.setName(UserRoleConstants.CUSTOMER);
                roleRepository.save(customerRole);
            }

            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder().encode("admin123"));
                Role adminRole = roleRepository.findByName(UserRoleConstants.ADMIN);
                Role customerRole = roleRepository.findByName(UserRoleConstants.CUSTOMER);
                admin.setRoles(Set.of(adminRole, customerRole));
                admin.setEmail("admin@admin.com");
                userRepository.save(admin);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}