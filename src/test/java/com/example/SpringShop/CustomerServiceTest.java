package com.example.SpringShop;


import com.example.SpringShop.Dto.Customer.*;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.EntityMappers.CustomerMapper;
import com.example.SpringShop.Exceptions.*;
import com.example.SpringShop.Repositories.*;
import com.example.SpringShop.Services.*;
import com.example.SpringShop.Utilities.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterDto registerDto = new RegisterDto("0123456789", "test" , "username", "password" , "password", "email@example.com");

        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
        when(customerRepository.existsByMobileNumber("0123456789")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        Customer registeredCustomer = customerService.register(registerDto);
        assertNotNull(registeredCustomer);
        assertEquals("username", registeredCustomer.getUser().getUsername());
        assertEquals("email@example.com", registeredCustomer.getUser().getEmail());
        assertEquals("0123456789", registeredCustomer.getMobileNumber());
        assertEquals("encodedPassword", registeredCustomer.getUser().getPassword());
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("existingUser");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            customerService.register(registerDto);
        });
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("existingEmail@test.com");

        when(userRepository.existsByEmail("existingEmail@test.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            customerService.register(registerDto);
        });
    }

    @Test
    void testRegister_MobileNumberAlreadyExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setMobileNumber("existingMobile");

        when(customerRepository.existsByMobileNumber("existingMobile")).thenReturn(true);

        assertThrows(MobileNumberAlreadyExistsException.class, () -> {
            customerService.register(registerDto);
        });
    }

    @Test
    void testLogin_Success() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("testPassword");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetailsService.loadUserByUsername(loginDto.getUsername())).thenReturn(userDetails);

        String token = "validJwtToken";
        when(jwtUtil.generateToken(userDetails.getUsername())).thenReturn(token);

        String result = customerService.login(loginDto);

        assertEquals(token, result);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        verify(userDetailsService).loadUserByUsername(loginDto.getUsername());
        verify(jwtUtil).generateToken(userDetails.getUsername());
    }

    @Test
    void testLogin_InvalidCredentials() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("wrongPassword");

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(InvalidCredentialsException.class, () -> customerService.login(loginDto));
    }

    @Test
    void testLogin_UnexpectedError() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("testPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Some unexpected error"));

        assertThrows(RuntimeException.class, () -> customerService.login(loginDto));
    }

    @Test
    void testChangeUsername_Success() {
        ChangeUsernameDto changeUsernameDto = new ChangeUsernameDto("currentUsername", "newUsername");
        User mockUser = new User();
        mockUser.setUsername("currentUsername");
        Customer mockCustomer = new Customer();
        mockCustomer.setUser(mockUser);
        when(userService.getUserByUsername("currentUsername")).thenReturn(mockUser);
        when(userRepository.findByUsername("newUsername")).thenReturn(null);
        when(customerRepository.findByUser(mockUser)).thenReturn(mockCustomer);


         Customer result = customerService.changeUsername(changeUsernameDto, "currentUsername");


        assertNotNull(result);
        assertEquals("newUsername", mockUser.getUsername());
        assertEquals(mockCustomer, result);
        verify(userRepository).save(mockUser);
    }

   @Test
    void testChangeUsername_IncorrectOldUsername() {

        ChangeUsernameDto changeUsernameDto = new ChangeUsernameDto("wrongUsername", "newUsername");
        User mockUser = new User();
        when(userService.getUserByUsername("currentUsername")).thenReturn(mockUser);

        assertThrows(WrongUsernameException.class, () -> {
            customerService.changeUsername(changeUsernameDto, "currentUsername");
        });
    }

    @Test
    void testChangeUsername_NewUsernameTaken() {
        ChangeUsernameDto changeUsernameDto = new ChangeUsernameDto("currentUsername", "takenUsername");

        User mockUser = new User();
        mockUser.setUsername("currentUsername");
        when(userService.getUserByUsername("currentUsername")).thenReturn(mockUser);

        User existingUser = new User();
        existingUser.setUsername("takenUsername");
        when(userRepository.findByUsername("takenUsername")).thenReturn(existingUser);
        assertThrows(UsernameAlreadyTakenException.class, () -> {
            customerService.changeUsername(changeUsernameDto, "currentUsername");
        });
    }

    @Test
    void testChangePassword_Success() {
        String username = "testUsername";

        ChangePasswordDto changePasswordDto = new ChangePasswordDto("oldPassword", "newPassword", "newPassword");

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword("encodedOldPassword");

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setUser(mockUser);

        when(userService.getUserByUsername(username)).thenReturn(mockUser);
        when(passwordEncoder.matches("oldPassword", mockUser.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("newPassword", mockUser.getPassword())).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(customerRepository.findByUser(mockUser)).thenReturn(mockCustomer);

        Customer result = customerService.changePassword(changePasswordDto, username);

        assertNotNull(result);
        assertEquals(mockCustomer, result);
        assertEquals("encodedNewPassword", mockUser.getPassword());
        verify(userRepository).save(mockUser);
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("wrongOldPassword", "newPassword", "newPassword");
        User mockUser = new User();
        mockUser.setPassword(passwordEncoder.encode("oldPassword"));

        when(userService.getUserByUsername("currentUsername")).thenReturn(mockUser);

        assertThrows(InvalidPasswordException.class, () -> {
            customerService.changePassword(changePasswordDto, "currentUsername");
        });
    }

    @Test
    void testGetCustomerDetails_UserNotFound() {
        String username = "nonExistingUsername";
        when(userService.getUserByUsername(username)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> {
            customerService.getCustomerDetails(username);
        });
    }

    @Test
    void testChangeMobileNumber_Success() {
        String username = "currentUsername";
        ChangeMobileNumberDto changeMobileNumberDto = new ChangeMobileNumberDto("oldMobileNumber", "newMobileNumber");

        User mockUser = new User();
        mockUser.setUsername(username);

        Customer mockCustomer = new Customer();
        mockCustomer.setUser(mockUser);
        mockCustomer.setMobileNumber("oldMobileNumber");

        when(userService.getUserByUsername(username)).thenReturn(mockUser);
        when(customerRepository.findByUser(mockUser)).thenReturn(mockCustomer);
        when(customerRepository.findByMobileNumber("newMobileNumber")).thenReturn(null);

        Customer updatedCustomer = customerService.changeMobileNumber(changeMobileNumberDto, username);
        assertNotNull(updatedCustomer);
        assertEquals("newMobileNumber", updatedCustomer.getMobileNumber());
        verify(customerRepository).save(updatedCustomer);
    }

    @Test
    void testChangeMobileNumber_InvalidOldNumber() {
        String username = "currentUsername";
        ChangeMobileNumberDto changeMobileNumberDto = new ChangeMobileNumberDto("wrongOldMobileNumber", "newMobileNumber");

        User mockUser = new User();
        mockUser.setUsername(username);

        Customer mockCustomer = new Customer();
        mockCustomer.setUser(mockUser);
        mockCustomer.setMobileNumber("oldMobileNumber");

        when(userService.getUserByUsername(username)).thenReturn(mockUser);
        when(customerRepository.findByUser(mockUser)).thenReturn(mockCustomer);

        assertThrows(InvalidMobileNumberException.class, () -> {
            customerService.changeMobileNumber(changeMobileNumberDto, username);
        });
    }

    @Test
    void testChangeMobileNumber_NewNumberSameAsOld() {
        String username = "currentUsername";
        ChangeMobileNumberDto changeMobileNumberDto = new ChangeMobileNumberDto("oldMobileNumber", "oldMobileNumber");

        User mockUser = new User();
        mockUser.setUsername(username);

        Customer mockCustomer = new Customer();
        mockCustomer.setUser(mockUser);
        mockCustomer.setMobileNumber("oldMobileNumber");

        when(userService.getUserByUsername(username)).thenReturn(mockUser);
        when(customerRepository.findByUser(mockUser)).thenReturn(mockCustomer);

        assertThrows(NewNumberSameLikeOldNumberException.class, () -> {
            customerService.changeMobileNumber(changeMobileNumberDto, username);
        });
    }

    @Test
    void testChangeMobileNumber_NumberAlreadyTaken() {
        String username = "currentUsername";
        ChangeMobileNumberDto changeMobileNumberDto = new ChangeMobileNumberDto("oldMobileNumber", "takenMobileNumber");

        User mockUser = new User();
        mockUser.setUsername(username);

        Customer mockCustomer = new Customer();
        mockCustomer.setUser(mockUser);
        mockCustomer.setMobileNumber("oldMobileNumber");

        Customer existingCustomer = new Customer();
        existingCustomer.setMobileNumber("takenMobileNumber");

        when(userService.getUserByUsername(username)).thenReturn(mockUser);
        when(customerRepository.findByUser(mockUser)).thenReturn(mockCustomer);
        when(customerRepository.findByMobileNumber("takenMobileNumber")).thenReturn(existingCustomer);

        assertThrows(MobileNumberAlreadyTakenException.class, () -> {
            customerService.changeMobileNumber(changeMobileNumberDto, username);
        });
    }
    @Test
    void testChangeEmail_Success() {
        String username = "currentUsername";
        ChangeEmailDto changeEmailDto = new ChangeEmailDto("oldEmail@example.com", "newEmail@example.com");

        User mockUser = new User();
        mockUser.setEmail("oldEmail@example.com");
        mockUser.setUsername(username);

        Customer mockCustomer = new Customer();
        mockCustomer.setUser(mockUser);

        when(userService.getUserByUsername(username)).thenReturn(mockUser);
        when(userRepository.findByEmail("newEmail@example.com")).thenReturn(null);
        when(customerRepository.findByUser(mockUser)).thenReturn(mockCustomer);

        Customer updatedCustomer = customerService.changeEmail(changeEmailDto, username);

        assertNotNull(updatedCustomer);
        assertEquals("newEmail@example.com", mockUser.getEmail());
        verify(userRepository).save(mockUser);
    }

    @Test
    void testChangeEmail_InvalidOldEmail() {
        String username = "currentUsername";
        ChangeEmailDto changeEmailDto = new ChangeEmailDto("wrongOldEmail@example.com", "newEmail@example.com");

        User mockUser = new User();
        mockUser.setEmail("oldEmail@example.com");
        mockUser.setUsername(username);

        when(userService.getUserByUsername(username)).thenReturn(mockUser);

        assertThrows(InvalidEmailException.class, () -> {
            customerService.changeEmail(changeEmailDto, username);
        });
    }

    @Test
    void testChangeEmail_NewEmailSameAsOld() {
        String username = "currentUsername";
        ChangeEmailDto changeEmailDto = new ChangeEmailDto("oldEmail@example.com", "oldEmail@example.com");

        User mockUser = new User();
        mockUser.setEmail("oldEmail@example.com");
        mockUser.setUsername(username);

        when(userService.getUserByUsername(username)).thenReturn(mockUser);

        assertThrows(NewEmailSameLikeOldEmailException.class, () -> {
            customerService.changeEmail(changeEmailDto, username);
        });
    }

    @Test
    void testChangeEmail_EmailAlreadyTaken() {
        String username = "currentUsername";
        ChangeEmailDto changeEmailDto = new ChangeEmailDto("oldEmail@example.com", "takenEmail@example.com");

        User mockUser = new User();
        mockUser.setEmail("oldEmail@example.com");
        mockUser.setUsername(username);

        User existingUser = new User();
        existingUser.setEmail("takenEmail@example.com");

        when(userService.getUserByUsername(username)).thenReturn(mockUser);
        when(userRepository.findByEmail("takenEmail@example.com")).thenReturn(existingUser);

        assertThrows(EmailAlreadyTakenException.class, () -> {
            customerService.changeEmail(changeEmailDto, username);
        });
    }
}
