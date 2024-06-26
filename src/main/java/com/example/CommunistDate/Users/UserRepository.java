package com.example.CommunistDate.Users;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUsername(String username);

    @Query(value = "SELECT * from users ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    User findRandomUser();

    @Query("SELECT u FROM User u WHERE u.id NOT IN :excludedUserIds ORDER BY RAND() LIMIT 1")
    User findRandomUserExcluding(@Param("excludedUserIds") List<Long> excludedUserIds);
}