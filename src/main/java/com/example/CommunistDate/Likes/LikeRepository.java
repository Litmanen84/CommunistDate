package com.example.CommunistDate.Likes;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {
   @Query("SELECT l FROM Like l WHERE l.userId1.id = :userId AND l.likes = true")
    List<Like> findLikesByUserId1(Long userId);

    @Query("SELECT l FROM Like l WHERE l.userId2.id = :userId AND l.likes = true")
    List<Like> findLikesByUserId2(Long userId);

}
