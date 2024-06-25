package com.example.CommunistDate.Likes;
import com.example.CommunistDate.Users.User;
import com.example.CommunistDate.Users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    public LikeService(LikeRepository likeRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public void choice(Long likingUserId, LikeRequest likeRequest) {
        User sender = userRepository.findById(likingUserId).orElseThrow(() -> new EntityNotFoundException("User with ID " + likingUserId + " not found"));
        User receiver = userRepository.findById(likeRequest.getLikedUser()).orElseThrow(() -> new EntityNotFoundException("User with ID " + likeRequest.getLikedUser() + " not found"));
        
        Like like = new Like();
        like.setUserId1(sender);
        like.setUserId2(receiver);
        like.setLikes(likeRequest.getLike());    
        try {
            likeRepository.save(like);
            logger.debug("Like saved successfully");
        } catch (Exception e) {
            logger.error("Error saving like", e);
            throw e;
        }
    }

    public List<User> getMatches(Long userId) {
        List<Like> likesByUser = likeRepository.findLikesByUserId1(userId);
        List<Long> likedUserIds = likesByUser.stream()
                .map(like -> like.getUserId2().getId())
                .collect(Collectors.toList());

        List<Like> likesReceived = likeRepository.findLikesByUserId2(userId);
        return likesReceived.stream()
                .filter(like -> likedUserIds.contains(like.getUserId1().getId()))
                .map(Like::getUserId1)
                .collect(Collectors.toList());
    }
}


