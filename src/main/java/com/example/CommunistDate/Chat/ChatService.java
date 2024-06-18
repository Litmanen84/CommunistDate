package com.example.CommunistDate.Chat;

import org.springframework.stereotype.Service;
import com.example.CommunistDate.Users.User;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void sendMessage(Long senderId, NewMessage newMessage) {
        User sender = new User();
        sender.setId(senderId);
        User receiver = new User();
        receiver.setId(newMessage.getReceiverId());
        Chat chat = new Chat(sender, receiver, newMessage.getContent());
        chatRepository.save(chat);
    }
    
}
