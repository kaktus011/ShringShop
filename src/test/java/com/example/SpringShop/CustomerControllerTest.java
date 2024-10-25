package com.example.SpringShop;

import com.example.SpringShop.Controllers.CustomerController;
import com.example.SpringShop.Dto.Customer.*;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.User;
import com.example.SpringShop.Exceptions.*;
import com.example.SpringShop.Services.CustomerService;
import com.example.SpringShop.Services.UserService;
import com.example.SpringShop.Utilities.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testLogout_Success() {
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;

        ResponseEntity<?> response = customerController.logout(authorizationHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully.", response.getBody());
        verify(jwtUtil).invalidateToken(token);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testLogout_MissingAuthorizationHeader() {
        ResponseEntity<?> response = customerController.logout(null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("You are not logged in.", response.getBody());
        verify(jwtUtil, never()).invalidateToken(anyString());
    }

    @Test
    void testLogout_InvalidAuthorizationHeader() {
        String authorizationHeader = "InvalidHeader";

        ResponseEntity<?> response = customerController.logout(authorizationHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("You are not logged in.", response.getBody());
        verify(jwtUtil, never()).invalidateToken(anyString());
    }

    @Test
    void testLogout_EmptyToken() {
        String authorizationHeader = "Bearer ";

        ResponseEntity<?> response = customerController.logout(authorizationHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("You are not logged in.", response.getBody());
        verify(jwtUtil, never()).invalidateToken(anyString());
    }
    @Test
    void testChangeUsername_Success() {
        ChangeUsernameDto changeUsernameDto = new ChangeUsernameDto("currentUsername", "newUsername");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("currentUsername");
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setName("test");
        User user = new User();
        user.setUsername("newUsername");
        mockCustomer.setUser(user);
        mockCustomer.setMobileNumber("1234567890");

        when(customerService.changeUsername(changeUsernameDto, "currentUsername")).thenReturn(mockCustomer);

        ResponseEntity<?> response = customerController.changeUsername(changeUsernameDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        CustomerDetailsDto responseBody = (CustomerDetailsDto) response.getBody();
        assertEquals("newUsername", responseBody.getUsername());
    }

    @Test
    void testChangeUsername_UserNotFound() {
        ChangeUsernameDto changeUsernameDto = new ChangeUsernameDto("currentUsername", "newUsername" );

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("currentUsername");
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(customerService.changeUsername(changeUsernameDto, "currentUsername"))
                .thenThrow(new UserNotFoundException());

        ResponseEntity<?> response = customerController.changeUsername(changeUsernameDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
    }

    @Test
    void testChangeUsername_WrongUsername() {
        ChangeUsernameDto changeUsernameDto = new ChangeUsernameDto("wrongUsername", "newUsername" );

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("currentUsername");
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(customerService.changeUsername(changeUsernameDto, "currentUsername"))
                .thenThrow(new WrongUsernameException());

        ResponseEntity<?> response = customerController.changeUsername(changeUsernameDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Wrong username for user.", response.getBody());
    }

    @Test
    void testChangeUsername_UsernameAlreadyTaken() {
        ChangeUsernameDto changeUsernameDto = new ChangeUsernameDto("wrongUsername", "takenUsername" );

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("currentUsername");
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        when(customerService.changeUsername(changeUsernameDto, "currentUsername"))
                .thenThrow(new UsernameAlreadyTakenException("takenUsername"));

        ResponseEntity<?> response = customerController.changeUsername(changeUsernameDto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Username 'takenUsername' is already taken.", response.getBody());
    }

    @Test
    void testChangePassword_Success() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("oldPassword", "newPassword", "newPassword");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("currentUsername");
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        User mockUser = new User();
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setUser(mockUser);
        mockUser.setPassword(passwordEncoder.encode("oldPassword"));

        when(customerService.changePassword(changePasswordDto, "currentUsername")).thenReturn(mockCustomer);

        ResponseEntity<?> response = customerController.changePassword(changePasswordDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockCustomer.getUser().getUsername(), ((CustomerDetailsDto) response.getBody()).getUsername());
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("wrongOldPassword", "newPassword", "newPassword");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("currentUsername");
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        User mockUser = new User();
        mockUser.setPassword(passwordEncoder.encode("oldPassword"));

        when(userService.getUserByUsername("currentUsername")).thenReturn(mockUser);

        doThrow(new InvalidPasswordException()).when(customerService).changePassword(changePasswordDto, "currentUsername");

        ResponseEntity<?> response = customerController.changePassword(changePasswordDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid password.", response.getBody());
    }

    @Test
    void testChangePassword_NewPasswordMatchesOldPassword() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("oldPassword", "oldPassword", "oldddPassword");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("currentUsername");
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        User mockUser = new User();
        mockUser.setPassword(passwordEncoder.encode("oldPassword"));

        doThrow(new PasswordMismatchException()).when(customerService).changePassword(changePasswordDto, "currentUsername");

        ResponseEntity<?> response = customerController.changePassword(changePasswordDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("New password should be different from the old password.", response.getBody());
    }

    @Test
    void testGetCustomerDetails_Success() {
        String username = "currentUsername";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
        Mockito.when(authentication.getName()).thenReturn(username);

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setName("Test");
        CustomerDetailsDto mockDetailsDto = new CustomerDetailsDto();
        mockDetailsDto.setId(mockCustomer.getId());
        mockDetailsDto.setName(mockCustomer.getName());

        when(customerService.getCustomerDetails(username)).thenReturn(mockDetailsDto);

        ResponseEntity<?> response = customerController.getCustomerDetails();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockDetailsDto, response.getBody());
    }



    @Test
    void testChangeMobileNumber_InvalidOldNumber() {
        ChangeMobileNumberDto changeMobileNumberDto = new ChangeMobileNumberDto("wrongOldMobileNumber", "newMobileNumber");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
        Mockito.when(authentication.getName()).thenReturn("currentUsername");

        when(customerService.changeMobileNumber(changeMobileNumberDto, "currentUsername"))
                .thenThrow(new InvalidMobileNumberException());

        ResponseEntity<?> response = customerController.changeMobileNumber(changeMobileNumberDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid mobile number.", response.getBody());
    }

    @Test
    void testChangeMobileNumber_NewNumberSameAsOld() {
        ChangeMobileNumberDto changeMobileNumberDto = new ChangeMobileNumberDto("oldMobileNumber", "oldMobileNumber");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
        Mockito.when(authentication.getName()).thenReturn("currentUsername");

        when(customerService.changeMobileNumber(changeMobileNumberDto, "currentUsername"))
                .thenThrow(new NewNumberSameLikeOldNumberException());

        ResponseEntity<?> response = customerController.changeMobileNumber(changeMobileNumberDto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("New mobile number should be different from old mobile number.", response.getBody());
    }

    @Test
    void testChangeMobileNumber_NumberAlreadyTaken() {
        ChangeMobileNumberDto changeMobileNumberDto = new ChangeMobileNumberDto("oldMobileNumber", "takenMobileNumber");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
        Mockito.when(authentication.getName()).thenReturn("currentUsername");

        when(customerService.changeMobileNumber(changeMobileNumberDto, "currentUsername"))
                .thenThrow(new MobileNumberAlreadyTakenException("takenMobileNumber"));

        ResponseEntity<?> response = customerController.changeMobileNumber(changeMobileNumberDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Mobile number 'takenMobileNumber' is already taken.", response.getBody());
    }
}