// package com.example.CommunistDate.Likes;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.CommunistDate.Users.UserRepository;

// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/likes")
// public class LikeController {
//     private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
//     private final LikeService likeService;
//     private final UserRepository userRepository;

//     @Autowired
//     public LikeController(LikeService likeService, UserRepository userRepository) {
//         this.likeService = likeService;
//         this.userRepository = userRepository;
//     }

//     @PostMapping("/choice")
//     public ResponseEntity<Object> choice(@Valid @RequestBody LikeRequest likeRequest) {
//         return ResponseEntity.ok("User liked");
//     }
// }
