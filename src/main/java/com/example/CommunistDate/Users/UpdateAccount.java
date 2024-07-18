package com.example.CommunistDate.Users;

import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateAccount {

    @Nullable
    private String newUsername;

    @Nullable
    private String email;

    @NonNull
    private String password;

    @Nullable
    @Size(min = 5, max = 25)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter and one number")
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
