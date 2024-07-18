package com.example.CommunistDate.Posts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.CommunistDate.Users.UserService;
import jakarta.validation.Valid;
import com.example.CommunistDate.Users.User;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService service;
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    PostController(PostService service, UserService userService) {
        this.userService = userService;
        this.service = service;
    } 

    @GetMapping("/")
    public ResponseEntity<List<Post>> getAllPosts(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            List<Post> posts = service.getAllPosts();
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // @GetMapping("/{id}")
    // public Post getPostById(@PathVariable Long id) {
    //     return service.getPostById(id)
    //             .orElseThrow(() -> new PostNotFoundException(id));
    // }

    @PostMapping("/")
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest request, Authentication auth, BindingResult result) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldError();
            if (fieldError != null) {
                logger.debug("Validation error on field, fess " + fieldError.getField() + ": " + fieldError.getDefaultMessage() + " " + fieldError.getCode());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        String username = jwt.getSubject();
        User user = userService.findByUsername(username);
        
        if (user != null) {
            Long id = user.getId();
            try {
                Post createdPost = service.createPost(request, id);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
            } catch (Exception e) { 
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


//     @PutMapping("/{id}")
//     public void updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest request) {
//         User user = userService.findByUsername(request.getUsername());
//         Post existingPost = service.getPostById(id)
//                 .orElseThrow(() -> new PostNotFoundException(id));
//         if (user != null && user.getIs_admin()) {
//             service.updatePost(id, request.getTitle(), request.getContent());
//         } else {
//             throw new UnauthorizedException("User is not authorized to update this post");
//         }
//     }

//     @DeleteMapping("/{id}")
//     public void deletePost(@PathVariable Long id, @RequestBody DeletePostRequest request) {
//         User user = userService.findByUsername(request.getUsername());
//         if (user != null && user.getIs_admin()) {
//             service.deletePost(id);
//         } else {
//             throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can delete posts");
//         }
//     }
}
