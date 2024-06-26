package com.example.CommunistDate.Chat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;
import com.example.CommunistDate.Users.User;

@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    public User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    public User receiver;

    @NotNull
    @Column(name = "content", nullable = false)
    public String content;

    @Column(nullable = true, name = "timestamp")
    public LocalDateTime createdAt = LocalDateTime.now();

    public Chat() {}

    public Chat (User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }   

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) && Objects.equals(sender, chat.sender) && Objects.equals(receiver, chat.receiver) && Objects.equals(content, chat.content) && Objects.equals(createdAt, chat.createdAt); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver, content, createdAt);
    }
}
