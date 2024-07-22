package com.example.CommunistDate.UserPreferences;
import com.example.CommunistDate.Users.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
    Optional<UserPreferences> findByUser(User user);
}

