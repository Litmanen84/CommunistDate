package com.example.CommunistDate.Posts;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.CommunistDate.Users.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
}
