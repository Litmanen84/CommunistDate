package com.example.CommunistDate.Posts;

import java.time.LocalDateTime;
import java.util.Objects;
import com.example.CommunistDate.Users.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @NotBlank(message = "Title is mandatory")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Content is mandatory")
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Post() {}

    public Post(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return this.id;
    }

    public User getUserId() {
        return this.user;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUserId(User user) {
        this.user = user;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(user, post.user) &&
                Objects.equals(title, post.title) &&
                Objects.equals(content, post.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, title, content);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + user +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
