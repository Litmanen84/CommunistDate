package com.example.CommunistDate.UserPreferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Set;
import com.example.CommunistDate.Likes.Like;
import com.example.CommunistDate.Users.User;
import com.example.CommunistDate.Users.UserRepository;
import com.example.CommunistDate.Likes.LikeRepository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserPreferencesController {

    private final UserRepository repository;
    private final UserPreferencesRepository preferencesRepository;
    private final LikeRepository likeRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserPreferencesController.class);
    
    @Autowired
    public UserPreferencesController(UserRepository repository, UserPreferencesRepository preferencesRepository, LikeRepository likeRepository) {
        this.repository = repository;
        this.preferencesRepository = preferencesRepository;
        this.likeRepository = likeRepository;
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

    @GetMapping("/preferences")
    public ResponseEntity<Object> getUserPreferences(Authentication auth) {
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

        UserPreferences existingPreferences = preferencesRepository.findByUser(user).orElse(null);
        if (existingPreferences != null) {
            return ResponseEntity.ok(existingPreferences);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preferences not found");
        }
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
}
