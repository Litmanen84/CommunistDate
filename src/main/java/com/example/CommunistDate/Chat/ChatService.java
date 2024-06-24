package com.example.CommunistDate.Chat;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.CommunistDate.Users.User;
import com.example.CommunistDate.Users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public void sendMessage(Long senderId, NewMessage newMessage) {
        Optional<User> optionalSender = userRepository.findById(senderId);
        Optional<User> optionalReceiver = userRepository.findById(newMessage.getReceiverId());
        if (optionalSender.isEmpty()) {
            throw new EntityNotFoundException("User with ID " + senderId + " not found");
        }
        if (optionalReceiver.isEmpty()) {
            throw new EntityNotFoundException("User with ID " + newMessage.getReceiverId() + " not found");
        }
        User sender = optionalSender.get();
        User receiver = optionalReceiver.get();

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setContent(newMessage.getContent());

        try {
            chatRepository.save(chat);
            logger.debug("Chat message saved successfully");
        } catch (Exception e) {
            logger.error("Error saving chat", e);
            throw e;
        }
    }
}