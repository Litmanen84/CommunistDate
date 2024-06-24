package com.example.CommunistDate.Chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.CommunistDate.Users.User;
import com.example.CommunistDate.Users.UserRepository;

import io.jsonwebtoken.lang.Collections;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private final UserRepository userRepository;

    @Autowired
    public ChatController(ChatService chatService, UserRepository userRepository) {
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    @GetMapping("/history/{userId1}/{userId2}")
    public List<Chat> getChatHistory(@PathVariable Long userId1, @PathVariable Long userId2, Authentication auth, BindingResult result) {
        if ((auth == null) || !(auth.getPrincipal() instanceof Jwt)) {
            logger.error("Ehi! Authentication object is null -1");
            return Collections.emptyList();
        }
    
        if (!auth.isAuthenticated()) {
            logger.error("Ehi! Authentication object is null -2");
            return Collections.emptyList();
        }
    
        if (result.hasErrors()) {
            logger.error("Ehi! Authentication object is null -3");
            return Collections.emptyList();
        }
    
        Jwt jwt = (Jwt) auth.getPrincipal();
        logger.debug("Here is the JWT instance: " + jwt);
        String username = jwt.getSubject();
        boolean isAdmin = jwt.getClaim("isAdmin");
        logger.debug("This is the Username looking at the conversation: " + username);
        logger.debug("Let's check your authorities: " + isAdmin); 
        return chatService.getChatHistory(userId1, userId2);
    }

    @MessageMapping("/send")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody NewMessage newMessage, BindingResult result, Authentication auth) {
    logger.debug("Starting sendMessage method...");
    if ((auth == null) || !(auth.getPrincipal() instanceof Jwt)) {
        logger.error("Ehi! Authentication object is null");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -1");
    }

    if (!auth.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -2");
    }
    
    if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(result.getAllErrors());
    }

    Jwt jwt = (Jwt) auth.getPrincipal();
        logger.debug("Here is the JWT instance: " + jwt);
        String username = jwt.getSubject();
        boolean isAdmin = jwt.getClaim("isAdmin");
        logger.debug("This is the Username: " + username);
        logger.debug("Let's check your authorities: " + isAdmin);

    Optional<User> sender = userRepository.findByUsername(username);

    if (sender.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -3");
    };

    Long senderId = sender.get().getId();

    try {
        chatService.sendMessage(senderId, newMessage);
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        logger.error("Error sending message", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message: " + e.getMessage());
        }
    }
}
