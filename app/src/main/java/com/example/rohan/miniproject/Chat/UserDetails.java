package com.example.rohan.miniproject.Chat;

/**
 * Created by Rohan on 01/04/17.
 */


public class UserDetails {
    private String username = "";

    public UserDetails() {
    }

    private String password = "";
    private String chatWith = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(String chatWith) {
        this.chatWith = chatWith;
    }
}