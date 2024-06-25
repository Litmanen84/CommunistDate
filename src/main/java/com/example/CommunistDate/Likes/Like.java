package com.example.CommunistDate.Likes;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.example.CommunistDate.Users.User;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeid")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userid1", nullable = false)
    public User userId1;

    @ManyToOne
    @JoinColumn(name = "userid2", nullable = false)
    public User userId2;

    @Column(name = "liked", nullable = true)
    public boolean likes;

    @Column(nullable = true, name = "timestamp")
    public LocalDateTime createdAt = LocalDateTime.now();

    public Like() {}

    public Like (User userId1, User userId2, boolean likes) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public User getUserId1() {
        return userId1;
    }

    public User getUserId2() {
        return userId2;
    }

    public boolean getLikes() {
        return likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId1(User userId1) {
        this.userId1 = userId1;
    }

    public void setUserId2(User userId2) {
        this.userId2 = userId2;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
