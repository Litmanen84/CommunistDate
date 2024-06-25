package com.example.CommunistDate.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUsername(String username);

    @Query(value = "SELECT * from users ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    User findRandomUser();
}