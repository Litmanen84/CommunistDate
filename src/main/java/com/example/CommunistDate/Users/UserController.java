package com.example.CommunistDate.Users;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.server.ResponseStatusException;
import com.example.CommunistDate.Likes.Like;
import com.example.CommunistDate.Likes.LikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepository repository;
  private final LikeRepository likeRepository;
  private final UserService service;
  private final Cloudinary cloudinary;
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  UserController(UserRepository repository, LikeRepository likeRepository, UserService service, Cloudinary cloudinary) {
    this.repository = repository;
    this.likeRepository = likeRepository;
    this.service = service;
    this.cloudinary = cloudinary;
  } 

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/all")
  public List<User> getAllUsers() {
    return repository.findAll();
  }

  @GetMapping("/random")
  public ResponseEntity<Object> getRandomUser(Authentication auth) {
    Optional<User> askingUserOptional = repository.findByUsername(auth.getName());
    if (!askingUserOptional.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    User askingUser = askingUserOptional.get();

    List<Like> userLikes = likeRepository.findAllByUserId1(askingUser);
    List<Long> excludedUserIds = userLikes.stream()
        .map(like -> like.getUserId2().getId())
        .collect(Collectors.toList());
    excludedUserIds.add(askingUser.getId());

    logger.info("Excluded User IDs: " + excludedUserIds);

    User user = repository.findRandomUserExcluding(excludedUserIds);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
    }
    logger.info("Selected User ID: " + user.getId());

    return ResponseEntity.ok(user);
}

  @GetMapping("/profile")
  public ResponseEntity<Object> personalProfile(Authentication auth) {
    var response = new HashMap<String, Object>();
    response.put("Username", auth.getName());
    response.put("Authorities", auth.getAuthorities());

    var user = repository.findByUsername(auth.getName());
    response.put("User", user);

    return ResponseEntity.ok(response);
  }  

  @PostMapping("/register")
  public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result) {
    if (result.hasErrors()) {
      FieldError fieldError = result.getFieldError();
      if (fieldError != null) {
        logger.debug("Validation error on field " + fieldError.getField() + ": " + fieldError.getDefaultMessage() + " " + fieldError.getCode());
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
    }

    try {
        System.out.println("No validation errors, proceeding with user creation");
        return ResponseEntity.ok(service.registerUser(registerRequest, result));
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
}

    @PostMapping("/login")
      public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginRequest loginRequest,
          BindingResult result) {
          logger.debug("Ma vaffanculo" + loginRequest.toString());
          try {
              return ResponseEntity.ok(service.loginUser(loginRequest, result));
          } catch (Exception e) {
              throw new ResponseStatusException(
                  HttpStatus.UNAUTHORIZED, e.getMessage(), e);
          }
      }

    @PutMapping("/{id}/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable Long id, @RequestParam("profilePicture") MultipartFile profilePicture, Authentication auth) {
      logger.debug("Starting uploading picture...");
      if ((auth == null) || !(auth.getPrincipal() instanceof Jwt)) {
          logger.error("Ehi! Authentication object is null");
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -1");
      }
      if (!auth.isAuthenticated()) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -2");
      }
      if (profilePicture.isEmpty() || !isValidFileType(profilePicture)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file");
    }
        try {
            Map uploadResult = cloudinary.uploader().upload(profilePicture.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");

            User user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setProfilePicture(url);
            repository.save(user);

            return ResponseEntity.ok("Profile picture uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading profile picture");
        }
    }

    @GetMapping("/{id}/profilePicture")
    public ResponseEntity<String> getProfilePicture(@PathVariable Long id) {
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getProfilePicture());
    }
    private boolean isValidFileType(MultipartFile file) {
      String contentType = file.getContentType();
      return contentType != null && contentType.startsWith("image");
  }
}