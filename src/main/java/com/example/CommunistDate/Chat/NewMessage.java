package com.example.CommunistDate.Chat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NewMessage {

    @NotNull
    @Size(min = 1, max = 1000)
    private String content;
    @NotNull
    private Long receiverId;

    public NewMessage(String content, Long receiverId) {
        this.content = content;
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
