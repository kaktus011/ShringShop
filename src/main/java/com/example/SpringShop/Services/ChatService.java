package com.example.SpringShop.Services;

import com.example.SpringShop.Dto.ChatDto;
import com.example.SpringShop.Dto.ChatOverviewDto;
import com.example.SpringShop.Dto.MessageDto;
import com.example.SpringShop.Entities.Chat;
import com.example.SpringShop.Entities.Customer;
import com.example.SpringShop.Entities.Message;
import com.example.SpringShop.Repositories.ChatRepository;
import com.example.SpringShop.Repositories.CustomerRepository;
import com.example.SpringShop.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final CustomerRepository customerRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, CustomerRepository customerRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.customerRepository = customerRepository;
        this.messageRepository = messageRepository;
    }

    public ChatDto sendMessage(Long senderId, Long receiverId, String content) {
        Customer sender = customerRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Customer receiver = customerRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Chat chat = chatRepository.findChatBetweenCustomers(senderId, receiverId);

        if (chat == null) {
            chat = new Chat();
            chat.setCustomerOne(sender);
            chat.setCustomerTwo(receiver);
            chat = chatRepository.save(chat);
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setDate(LocalDateTime.now());
        message.setChat(chat);
        messageRepository.save(message);

        List<MessageDto> messages = chat.getMessages().stream()
                .map(msg -> new MessageDto(
                        msg.getSender().getName(),
                        msg.getContent(),
                        msg.getDate()))
                .collect(Collectors.toList());

        return new ChatDto(chat.getId(), messages);
    }

    public List<ChatOverviewDto> getAllChats(Long customerId){
        List<Chat> chats = chatRepository.findAllChatsForCustomer(customerId);

        return chats.stream().map(chat -> {
            String otherPersonName;
            Long otherPersonId;
            if (chat.getCustomerOne().getId().equals(customerId)) {
                otherPersonName = chat.getCustomerTwo().getName();
                otherPersonId = chat.getCustomerTwo().getId();
            } else {
                otherPersonName = chat.getCustomerOne().getName();
                otherPersonId = chat.getCustomerOne().getId();
            }
            LocalDateTime lastSentMessage = chat.getMessages().isEmpty() ? null : chat.getMessages().get(chat.getMessages().size() - 1).getDate();

            return new ChatOverviewDto(chat.getId(), otherPersonName, otherPersonId, lastSentMessage);
        }).collect(Collectors.toList());

    }
}
