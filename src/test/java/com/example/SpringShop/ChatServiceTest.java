package com.example.SpringShop;

import com.example.SpringShop.Dto.Chat.ChatDto;
import com.example.SpringShop.Dto.Chat.ChatOverviewDto;
import com.example.SpringShop.Entities.Chat;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.Message;
import com.example.SpringShop.Exceptions.ChatNotFoundException;
import com.example.SpringShop.Exceptions.CustomerDoesNotHaveChatException;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Repositories.ChatRepository;
import com.example.SpringShop.Repositories.CustomerRepository;
import com.example.SpringShop.Repositories.MessageRepository;
import com.example.SpringShop.Services.ChatService;
import com.example.SpringShop.Services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatServiceTest {
    @InjectMocks
    private ChatService chatService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private CustomerService customerService;

    private Customer sender;
    private Customer receiver;


    private Long senderId;
    private Long receiverId;
    private String messageContent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sender = new Customer();
        sender.setId(1L);
        sender.setName("Sender Name");

        receiver = new Customer();
        receiver.setId(2L);
        receiver.setName("Receiver Name");

        senderId = sender.getId();
        receiverId = receiver.getId();
        messageContent = "Hello!";
    }

    @Test
    void testSendMessage_SenderNotFound() {
        when(customerRepository.findById(senderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            chatService.sendMessage(senderId, receiverId, messageContent);
        });
        assertEquals("Sender not found.", exception.getMessage());
    }

    @Test
    void testSendMessage_ReceiverNotFound() {
        when(customerRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(customerRepository.findById(receiverId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            chatService.sendMessage(senderId, receiverId, messageContent);
        });
        assertEquals("Receiver not found.", exception.getMessage());
    }

    @Test
    void testSendMessage_ChatExistsAndSendsMessage() {
        sender = new Customer();
        sender.setId(1L);
        sender.setName("Sender");

        receiver = new Customer();
        receiver.setId(2L);
        receiver.setName("Receiver");

        var chat = new Chat();
        chat.setId(1L);
        chat.setCustomerOne(sender);
        chat.setCustomerTwo(receiver);
        chat.setMessages(new ArrayList<>());
        when(customerRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(customerRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(chatRepository.findChatBetweenCustomers(sender.getId(), receiver.getId())).thenReturn(chat);
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message message = invocation.getArgument(0);
            chat.getMessages().add(message);
            return message;
        });

        ChatDto result = chatService.sendMessage(sender.getId(), receiver.getId(), "Hi again!");
        assertNotNull(result);
        assertEquals(chat.getId(), result.getId());
        assertEquals("Sender", result.getMessages().get(0).getSenderName());
        assertEquals("Hi again!", result.getMessages().get(0).getContent());
    }
    @Test
    public void testGetAllChats_WithChats() {
        Long customerId = 1L;
        Chat chat1 = new Chat();
        chat1.setId(1L);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Alice");
        chat1.setCustomerOne(customer1);

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Bob");
        chat1.setCustomerTwo(customer2);

        Message message = new Message();
        message.setDate(LocalDateTime.now());
        chat1.setMessages(Arrays.asList(message));

        when(chatRepository.findAllChatsForCustomer(customerId)).thenReturn(Arrays.asList(chat1));

        List<ChatOverviewDto> result = chatService.getAllChats(customerId);
        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).getOtherPersonName());
        assertEquals(2L, result.get(0).getOtherPersonId());
    }

    @Test
    public void testGetAllChats_NoChats() {
        Long customerId = 1L;
        when(chatRepository.findAllChatsForCustomer(customerId)).thenReturn(Collections.emptyList());
        List<ChatOverviewDto> result = chatService.getAllChats(customerId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetChatById_Success() {
        String username = "testuser";
        Long customerId = 1L;
        Long chatId = 1L;

        when(customerService.getCustomerId(username)).thenReturn(customerId);
        Chat chat = new Chat();
        chat.setId(chatId);
        var message = new Message();
        message.setDate(LocalDateTime.now());
        message.setContent("Hello");
        message.setSender(sender);
        message.setReceiver(receiver);
        chat.setMessages(Arrays.asList(message));

        when(chatRepository.findChatById(chatId)).thenReturn(chat);
        when(chatRepository.chatExistsForCustomerWithId(customerId)).thenReturn(true);

        ChatDto chatDto = chatService.getChatById(username, chatId);
        assertNotNull(chatDto);
        assertEquals(chatId, chatDto.getId());
        assertFalse(chatDto.getMessages().isEmpty());
    }

    @Test
    public void testGetChatById_ChatNotFound() {
        String username = "testuser";
        Long customerId = 1L;
        Long chatId = 1L;

        when(customerService.getCustomerId(username)).thenReturn(customerId);
        when(chatRepository.findChatById(chatId)).thenReturn(null);
        assertThrows(ChatNotFoundException.class, () -> chatService.getChatById(username, chatId));
    }

    @Test
    public void testGetChatById_CustomerDoesNotHaveAccess() {
        String username = "testuser";
        Long customerId = 1L;
        Long chatId = 1L;

        when(customerService.getCustomerId(username)).thenReturn(customerId);

        Chat chat = new Chat();
        chat.setId(chatId);

        when(chatRepository.findChatById(chatId)).thenReturn(chat);
        when(chatRepository.chatExistsForCustomerWithId(customerId)).thenReturn(false);
        assertThrows(CustomerDoesNotHaveChatException.class, () -> chatService.getChatById(username, chatId));
    }
}
