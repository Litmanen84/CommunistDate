package com.example.CommunistDate.Users;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.server.ResponseStatusException;
import com.example.CommunistDate.Likes.Like;
import com.example.CommunistDate.Likes.LikeRepository;
import com.example.CommunistDate.UserPreferences.UserPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Pageable;
import com.example.CommunistDate.UserPreferences.UserPreferencesRepository;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepository repository;
  private final LikeRepository likeRepository;
  private final UserPreferencesRepository preferencesRepository;
  private final UserService service;
  private final Cloudinary cloudinary;
  private final PasswordEncoder crypt;
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  UserController(UserRepository repository, LikeRepository likeRepository, UserService service, Cloudinary cloudinary, PasswordEncoder crypt, UserPreferencesRepository preferencesRepository) {
    this.repository = repository;
    this.likeRepository = likeRepository;
    this.service = service;
    this.cloudinary = cloudinary;
    this.crypt = crypt;
    this.preferencesRepository = preferencesRepository;
  } 

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/all")
  public List<User> getAllUsers() {
    return repository.findAll();
  }

  @GetMapping("/random")
  public ResponseEntity<Object> getRandomUser(Authentication auth) {
      if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update your profile, baccalà -1");
      }
      if (!auth.isAuthenticated()) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update your profile, baccalà -2");
      }
      Optional<User> askingUserOptional = repository.findByUsername(auth.getName());
      if (!askingUserOptional.isPresent()) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }
      User askingUser = askingUserOptional.get();

      Optional<UserPreferences> preferencesOptional = preferencesRepository.findByUser(askingUser);
      UserPreferences preferences = preferencesOptional.orElse(new UserPreferences());

      List<Like> userLikes = likeRepository.findAllByUserId1(askingUser);
      Set<Long> excludedUserIds = userLikes.stream()
          .map(like -> like.getUserId2().getId())
          .collect(Collectors.toSet());
      excludedUserIds.add(askingUser.getId());

      logger.info("Excluded User IDs: " + excludedUserIds);

      Pageable pageable = PageRequest.of(0, 1);
      List<User> users = repository.findRandomUserExcluding(
          new ArrayList<>(excludedUserIds),
          preferences.getMinAge(),
          preferences.getMaxAge(),
          preferences.getPoliticalBelief(),
          preferences.getGender(),
          preferences.getPartnerShare(),
          pageable
      );

      if (users.isEmpty()) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
      }
      User user = users.get(0);
      logger.info("Selected User ID: " + user.getId());

      return ResponseEntity.ok(user);
  }

  @PutMapping("/preferences")
  public ResponseEntity<Object> updateUserPreferences(Authentication auth, @RequestBody UserPreferences newPreferences) {
    if ((auth == null) || !(auth.getPrincipal() instanceof Jwt)) {
        logger.error("Ehi! Authentication object is null");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -1");
    }
    if (!auth.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -2");
    }
      Optional<User> userOptional = repository.findByUsername(auth.getName());
      if (!userOptional.isPresent()) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }
      User user = userOptional.get();

      // Check if preferences exist
      UserPreferences existingPreferences = preferencesRepository.findByUser(user).orElse(null);
      if (existingPreferences != null) {
          // Update existing preferences
          existingPreferences.setMinAge(newPreferences.getMinAge());
          existingPreferences.setMaxAge(newPreferences.getMaxAge());
          existingPreferences.setPoliticalBelief(newPreferences.getPoliticalBelief());
          existingPreferences.setGender(newPreferences.getGender());
          existingPreferences.setPartnerShare(newPreferences.getPartnerShare());
          preferencesRepository.save(existingPreferences);
      } else {
          // Create new preferences if not exist
          newPreferences.setUser(user);
          preferencesRepository.save(newPreferences);
      }

      return ResponseEntity.ok("Preferences updated");
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
  
  @GetMapping("/profile/{id}")
  public ResponseEntity<Object> profile(@PathVariable long id, Authentication auth) {
    if ((auth == null) || !(auth.getPrincipal() instanceof Jwt)) {
      logger.error("Ehi! Authentication object is null");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -1");
    }
    if (!auth.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -2");
    }
    
    try {
    User user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
      } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
      }
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

  @PutMapping("/{id}/updateAccount")
    public ResponseEntity<?> updateAccount(@PathVariable long id, @Validated @RequestBody UpdateAccount updateAccount, Authentication auth, BindingResult result) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldError();
            if (fieldError != null) {
                logger.debug("Validation error on field, baccalà " + fieldError.getField() + ": " + fieldError.getDefaultMessage() + " " + fieldError.getCode());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update your profile, baccalà -1");
        }
        if (!auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update your profile, baccalà -2");
        }

        User user = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found, baccalà"));

        if (!auth.getName().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update this profile, baccalà");
        }

        if (!crypt.matches(updateAccount.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Insert a valid password, baccalà");
        } else {
                try {
                    if (StringUtils.hasText(updateAccount.getNewUsername())) {
                      if (!repository.existsByUsername(updateAccount.getNewUsername())) {
                        if (updateAccount.getNewUsername().length() < 5 || updateAccount.getNewUsername().length() > 20) {
                          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New username must be between 5 and 20 characters");
                        }
                        user.setUsername(updateAccount.getNewUsername());
                      }
                    }
                    if (StringUtils.hasText(updateAccount.getEmail())) {
                      String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                      if (!updateAccount.getEmail().matches(emailRegex)) {
                          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
                      }
                      user.setEmail(updateAccount.getEmail());
                  }
                    if (StringUtils.hasText(updateAccount.getNewPassword())) {
                      if (updateAccount.getNewPassword().length() < 5 || updateAccount.getNewPassword().length() > 25) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password must be between 5 and 25 characters");
                    }
            
                      if (!updateAccount.getNewPassword().matches("^(?=.*[A-Z])(?=.*\\d).+$")) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must contain at least one uppercase letter and one number");
                    }
                        user.setPassword(crypt.encode(updateAccount.getNewPassword()));
                    }

                    repository.save(user);
                    return ResponseEntity.ok("User updated successfully");
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
                }
              }
    }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateUser(@PathVariable long id, @Validated @RequestBody UpdateInfo updateInfo, Authentication auth, BindingResult result) {
    if (result.hasErrors()) {
      FieldError fieldError = result.getFieldError();
      if (fieldError != null) {
        logger.debug("Validation error on field " + fieldError.getField() + ": " + fieldError.getDefaultMessage() + " " + fieldError.getCode());
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
    }  
    if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update your profile -1");
      }
    if (!auth.isAuthenticated()) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update your profile -2");
      }
      
    User user = repository.findByUsername(auth.getName())
              .orElseThrow(() -> new RuntimeException("User not found"));
      
    if (!auth.getName().equals(user.getUsername())) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update this profile");
      }
      
    try {
        if (StringUtils.hasText(updateInfo.getGender())) {
            user.setGender(updateInfo.getGender());
        }
        if (StringUtils.hasText(updateInfo.getCity())) {
            user.setCity(updateInfo.getCity());
        }
        if (StringUtils.hasText(updateInfo.getCountryOfResidence())) {
            user.setCountryOfResidence(updateInfo.getCountryOfResidence());
        }
        if (StringUtils.hasText(updateInfo.getLanguage())) {
            user.setLanguage(updateInfo.getLanguage());
        }
        if (StringUtils.hasText(updateInfo.getPoliticalBelief())) {
            user.setPoliticalBelief(updateInfo.getPoliticalBelief());
        }
        if (updateInfo.getCommunismLevel() != null) {
            user.setCommunismLevel(updateInfo.getCommunismLevel());
        }
        if (!Objects.equals(updateInfo.getPartnerShare(), null)) {
            user.setPartnerShare(updateInfo.getPartnerShare());
        }

        if (StringUtils.hasText(updateInfo.getDescription())) {
            user.setDescription(updateInfo.getDescription());
        }
        
        repository.save(user);
        return ResponseEntity.ok("User updated successfully");
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
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

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteUser(@PathVariable long id, Authentication auth) {
      if ((auth == null) || !(auth.getPrincipal() instanceof Jwt)) {
        logger.error("Ehi! Authentication object is null");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -1");
      }
      if (!auth.isAuthenticated()) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a message -2");
      }
      User currentUser = repository.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("User not found"));
      if (!auth.getName().equals(currentUser.getUsername()) && !currentUser.getIsAdmin()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be an admin to delete a user");
      } else if (auth.getName().equals(currentUser.getUsername()) || currentUser.getIsAdmin()) {
      try {
        User userToDelete = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        repository.delete(userToDelete);
        return ResponseEntity.ok("User deleted successfully");
      } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
      }
    }
    return ResponseEntity.ok().build();
    }
}