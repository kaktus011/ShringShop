package com.example.SpringShop;

import com.example.SpringShop.Controllers.ChatController;
import com.example.SpringShop.Dto.Chat.ChatDto;
import com.example.SpringShop.Dto.Chat.ChatOverviewDto;
import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Exceptions.ChatNotFoundException;
import com.example.SpringShop.Exceptions.CustomerDoesNotHaveChatException;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.UserNotFoundException;
import com.example.SpringShop.Services.ChatService;
import com.example.SpringShop.Services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChatControllerTest {
    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatService chatService;

    @Mock
    private CustomerService customerService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testSendMessage_Success() {
        Long receiverId = 2L;
        String content = "Hello!";
        Long senderId = 1L;
        String username = "testUser";
        ChatDto chatDto = new ChatDto(1L, new ArrayList<>());
        chatDto.setId(1L);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(customerService.getCustomerId(username)).thenReturn(senderId);
        when(chatService.sendMessage(senderId, receiverId, content)).thenReturn(chatDto);

        ResponseEntity<?> response = chatController.sendMessage(receiverId, content);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatDto, response.getBody());
    }

    @Test
    void testSendMessage_UserNotFound() {
        Long receiverId = 2L;
        String content = "Hello!";
        String username = "testUser";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(customerService.getCustomerId(username)).thenThrow(new UserNotFoundException());

        ResponseEntity<?> response = chatController.sendMessage(receiverId, content);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    void testSendMessage_CustomerNotFound() {
        Long receiverId = 2L;
        String content = "Hello!";
        Long senderId = 1L;
        String username = "testUser";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(customerService.getCustomerId(username)).thenReturn(senderId);
        when(chatService.sendMessage(senderId, receiverId, content)).thenThrow(new CustomerNotFoundException("Customer not found."));

        ResponseEntity<?> response = chatController.sendMessage(receiverId, content);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }
    @Test
    public void testGetAllChats_NoChats_ReturnsEmptyList() {
        when(customerService.getCustomerId("testUser")).thenReturn(1L);
        when(chatService.getAllChats(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = chatController.getAllChats();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    public void testGetAllChats_HasChats_ReturnsChatOverviewDtoList() {
        when(customerService.getCustomerId("testUser")).thenReturn(1L);
        ChatOverviewDto chatDto = new ChatOverviewDto(1L, "Receiver", 2L, LocalDateTime.now());
        when(chatService.getAllChats(1L)).thenReturn(List.of(chatDto));

        ResponseEntity<?> response = chatController.getAllChats();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        assertEquals("Receiver", ((List<ChatOverviewDto>) response.getBody()).get(0).getOtherPersonName());
    }

    @Test
    public void testGetAllChats_UserNotFound_ReturnsNotFound() {
        when(customerService.getCustomerId("testUser")).thenThrow(new UserNotFoundException());

        ResponseEntity<?> response = chatController.getAllChats();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    public void testGetChatById_Success() {
        Long chatId = 1L;
        String username = "testUser";
        Long senderId = 1L;

        ChatDto chatDto = new ChatDto(chatId, new ArrayList<>());
        when(customerService.getCustomerId(username)).thenReturn(senderId);
        when(chatService.getChatById(username, chatId)).thenReturn(chatDto);

        ResponseEntity<?> response = chatController.getChatById(chatId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatDto, response.getBody());
    }

    @Test
    public void testGetChatById_ChatNotFound() {
        Long chatId = 1L;
        String username = "testUser";
        Long senderId = 1L;

        when(customerService.getCustomerId(username)).thenReturn(senderId);
        when(chatService.getChatById(username, chatId)).thenThrow(new ChatNotFoundException());
        ResponseEntity<?> response = chatController.getChatById(chatId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
    }

    @Test
    public void testGetChatById_CustomerDoesNotHaveAccess() {
        Long chatId = 1L;
        String username = "testUser";
        Long senderId = 1L;

        when(customerService.getCustomerId(username)).thenReturn(senderId);
        when(chatService.getChatById(username, chatId)).thenThrow(new CustomerDoesNotHaveChatException("Access denied"));

        ResponseEntity<?> response = chatController.getChatById(chatId);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDto);
    }
}
