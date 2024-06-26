package com.example.CommunistDate.Chat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	
	List<Chat> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

	@Query("SELECT c FROM Chat c WHERE (c.sender.id = :userId1 AND c.receiver.id = :userId2) OR (c.sender.id = :userId2 AND c.receiver.id = :userId1)")
    List<Chat> findChatHistory(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
