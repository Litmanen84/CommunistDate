package com.example.CommunistDate.Posts;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.CommunistDate.Users.User;
import com.example.CommunistDate.Users.UserRepository;
import java.util.List;

@Service
public class PostService {

    private PostRepository repository;
    private UserRepository userRepository;

    @Autowired
    PostService(PostRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }    

    public List<Post> getAllPosts(User user) {
        return repository.findByUser(user);
    }

    public List<Post> getPostById(Long id) {
        User user = userRepository.findById(id).get();
        return repository.findByUser(user);
    }

    public Post createPost(CreatePostRequest request, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Fess, User not found for Id: " + id);
        }
    
        User user = optionalUser.get();

        Post post = new Post();
        post.setUserId(user);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
    
        return repository.save(post);
    }

    public Post updatePost(Long id, UpdatePostRequest update) {
        Post post = repository.findById(id).get();
        if (post == null) {
            throw new IllegalArgumentException("Fess, Post not found for Id: " + id);
        }
        post.setTitle(update.getTitle());
        post.setContent(update.getContent());
        return repository.save(post);
    }

    // public void deletePost(Long id) {
    //     if (repository.existsById(id)) {
    //         repository.deleteById(id);
    //     } else {
    //         throw new PostNotFoundException(id);
    //     }
    // }

    public Post findPostById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
