package com.broker.user_service.DTOs;

public class UserDTO {
    private String username;
    private String email;

    // Constructor
    public UserDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters y setters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}