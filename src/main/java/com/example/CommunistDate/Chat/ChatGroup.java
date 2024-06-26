package com.example.CommunistDate.Chat;

import java.util.List;
import com.example.CommunistDate.Users.User;

public class ChatGroup {
    private User user;
    private List<Chat> chats;

    public ChatGroup(User user, List<Chat> chats) {
        this.user = user;
        this.chats = chats;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
}
