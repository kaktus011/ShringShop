package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.Chat.ChatDto;
import com.example.SpringShop.Dto.Chat.ChatOverviewDto;
import com.example.SpringShop.Dto.Error.ErrorResponseDto;
import com.example.SpringShop.Exceptions.ChatNotFoundException;
import com.example.SpringShop.Exceptions.CustomerDoesNotHaveChatException;
import com.example.SpringShop.Exceptions.CustomerNotFoundException;
import com.example.SpringShop.Exceptions.UserNotFoundException;
import com.example.SpringShop.Services.ChatService;
import com.example.SpringShop.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final CustomerService customerService;

    @Autowired
    public ChatController(ChatService chatService, CustomerService customerService) {
        this.chatService = chatService;
        this.customerService = customerService;
    }

    @Secured({"ROLE_CUSTOMER", "ROLE_ADMIN"})
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestParam Long receiverId, @RequestParam String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try{
            Long senderId = customerService.getCustomerId(username);
            ChatDto chatDto = chatService.sendMessage(senderId, receiverId, content);
            return ResponseEntity.ok(chatDto);
        }catch (UserNotFoundException | CustomerNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred with sending message.");
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all-chats")
    public ResponseEntity<?> getAllChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try{
            Long senderId = customerService.getCustomerId(username);
            List<ChatOverviewDto> chats = chatService.getAllChats(senderId);
            return ResponseEntity.ok(chats);
        }catch (UserNotFoundException | CustomerNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred with retrieving chats.");
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<?> getChatById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try{
            ChatDto chat = chatService.getChatById(username, id);
            return ResponseEntity.ok(chat);
        }catch (UserNotFoundException | CustomerNotFoundException | ChatNotFoundException ex) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch(CustomerDoesNotHaveChatException ex){
            ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred with retrieving chats.");
        }
    }
}
