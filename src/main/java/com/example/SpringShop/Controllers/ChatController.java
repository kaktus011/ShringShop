package com.example.SpringShop.Controllers;

import com.example.SpringShop.Dto.ChatDto;
import com.example.SpringShop.Dto.ChatOverviewDto;
import com.example.SpringShop.Services.ChatService;
import com.example.SpringShop.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/message")
    public ResponseEntity<ChatDto> sendMessage(@RequestParam Long receiverId, @RequestParam String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long senderId = customerService.getCustomerId(username);

        ChatDto chatDto = chatService.sendMessage(senderId, receiverId, content);
        return ResponseEntity.ok(chatDto);
    }

    @GetMapping("/all-chats")
    public ResponseEntity<List<ChatOverviewDto>> getAllChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long senderId = customerService.getCustomerId(username);

        List<ChatOverviewDto> chats = chatService.getAllChats(senderId);
        return ResponseEntity.ok(chats);
    }
}
