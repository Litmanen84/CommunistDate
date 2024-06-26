package com.example.CommunistDate.Chat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	List<Chat> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
	List<Chat> findBySenderIdAndReceiverIdOrReceiverIdAltAndSenderIdAlt(Long senderId, Long receiverId, Long receiverIdAlt, Long senderIdAlt);
}
