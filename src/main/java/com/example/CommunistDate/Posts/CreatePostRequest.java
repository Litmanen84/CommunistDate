package com.example.CommunistDate.Posts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePostRequest {

    @NotNull
    @Size(min = 4, max = 255)
    private String title;

    @NotNull
    @Size(min = 10, max = 1000)
    private String content;
    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
