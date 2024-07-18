package com.example.CommunistDate.Posts;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.CommunistDate.Users.User;
import com.example.CommunistDate.Users.UserRepository;

@Service
public class PostService {

    private PostRepository repository;
    private UserRepository userRepository;

    @Autowired
    PostService(PostRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }    

    // public List<Post> getAllPosts() {
    //     return repository.findAll();
    // }

    // public Optional<Post> getPostById(Long id) {
    //     return repository.findById(id);
    // }

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

    // public Post updatePost(Long id, String title, String content) {
    //     return repository.findById(id)
    //             .map(post -> {
    //                 post.setTitle(title);
    //                 post.setContent(content);
    //                 return repository.save(post);
    //             })
    //             .orElseThrow(() -> new PostNotFoundException(id));
    // }

    // public void deletePost(Long id) {
    //     if (repository.existsById(id)) {
    //         repository.deleteById(id);
    //     } else {
    //         throw new PostNotFoundException(id);
    //     }
    // }
}
