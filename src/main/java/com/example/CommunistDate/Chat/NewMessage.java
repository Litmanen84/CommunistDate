package com.example.CommunistDate.Chat;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NewMessage {
    @NotNull
    @Size(min = 1, max = 1000)
    private String content;
    @NotNull
    private Long receiverId;
    private LocalDateTime createdAt;

    public NewMessage(String content, Long receiverId, LocalDateTime createdAt) {
        this.content = content;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
