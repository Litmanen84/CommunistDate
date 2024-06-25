package com.example.CommunistDate.Likes;

import jakarta.validation.constraints.NotNull;

public class LikeRequest {
    @NotNull
    private Long likedUser;
    @NotNull
    private boolean like;

    public LikeRequest(Long likedUser, boolean like) {
        this.likedUser = likedUser;
        this.like = like;
    }

    public Long getLikedUser() {
        return likedUser;
    }

    public boolean getLike() {
        return like;
    }

    public void setLikedUser(Long likedUser) {
        this.likedUser = likedUser;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
