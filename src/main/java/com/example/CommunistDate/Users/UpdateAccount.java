package com.example.CommunistDate.Users;

import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.Nullable;

public class UpdateAccount {

    @Nullable
    private String newUsername;

    @Nullable
    private String email;

    @NonNull
    private String password;

    @Nullable
    private String newPassword;

    public UpdateAccount() {}

    public UpdateAccount(String newUsername, String email, String password, String newPassword) {
        this.newUsername = newUsername;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
