package com.example.CommunistDate.Likes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.CommunistDate.Users.UserRepository;
import com.example.CommunistDate.Users.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/likes")
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    private final LikeService likeService;
    private final UserRepository userRepository;

    @Autowired
    public LikeController(LikeService likeService, UserRepository userRepository) {
        this.likeService = likeService;
        this.userRepository = userRepository;
    }

    @PostMapping("/choice")
    public ResponseEntity<Object> choice(@Valid @RequestBody LikeRequest likeRequest, BindingResult result, Authentication auth) {
        if ((auth == null) || !(auth.getPrincipal() instanceof Jwt)) {
        logger.error("Ehi! Authentication object is null");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -1");
        }
        if (!auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -2");
        }
        if (result.hasErrors()) {
        FieldError fieldError = result.getFieldError();
            if (fieldError != null) {
                logger.debug("Validation error on field " + fieldError.getField() + ": " + fieldError.getDefaultMessage() + " " + fieldError.getCode());
            }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        Jwt jwt = (Jwt) auth.getPrincipal();
        String username = jwt.getSubject();
        var likingUser = userRepository.findByUsername(username);
        if (likingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -3");
        };
    
        Long likingUserId = likingUser.get().getId();
        try {
            likeService.choice(likingUserId, likeRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error sending like", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message: " + e.getMessage());
            }
        }

    @GetMapping("/matches/{userId}")
    public List<User> getMatches(@PathVariable Long userId) {
        return likeService.getMatches(userId);
    }
}
