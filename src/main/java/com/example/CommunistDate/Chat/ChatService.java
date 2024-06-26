package com.example.CommunistDate.Chat;

import org.springframework.stereotype.Service;
import com.example.CommunistDate.Users.User;
import com.example.CommunistDate.Users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public List<Chat> getChatHistory(Long userId1, Long userId2) {
        return chatRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(userId1, userId2, userId2, userId1);
    }

    public Chat sendMessage(Long senderId, NewMessage newMessage) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new EntityNotFoundException("User with ID " + senderId + " not found"));
        User receiver = userRepository.findById(newMessage.getReceiverId()).orElseThrow(() -> new EntityNotFoundException("User with ID " + newMessage.getReceiverId() + " not found"));
        logger.debug("Sender: " + sender.getUsername() + " Receiver: " + receiver.getUsername());
        logger.debug("Message: " + newMessage.getContent());
        Chat chat= new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setContent(newMessage.getContent());
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }
}