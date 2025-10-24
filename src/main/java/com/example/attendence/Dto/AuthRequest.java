package com.example.attendence.Dto;

public class AuthRequest {
    private String username;
    private String password;

    // Getters and Setters (omitted for brevity)
    // Make sure to generate them!


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
}