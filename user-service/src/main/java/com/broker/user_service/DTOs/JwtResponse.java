package com.broker.user_service.DTOs;

import com.broker.user_service.model.User;
import com.broker.user_service.model.UserPermission;
import com.broker.user_service.model.UserProfile;
import com.broker.user_service.utils.ProductPermission;

import java.util.List;

public class JwtResponse {

    private String token;
    private String username;
    private Long userId;
    private Boolean hasPersonalData;
    private String fullName;
    private List<ProductPermission> permissions;

    // Constructor


    public JwtResponse(String token, String username, Long userId, Boolean hasPersonalData, String fullName, List<ProductPermission> permissions) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.hasPersonalData = hasPersonalData;
        this.fullName = fullName;
        this.permissions = permissions;
    }


    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getHasPersonalData() {
        return hasPersonalData;
    }

    public void setHasPersonalData(Boolean hasPersonalData) {
        this.hasPersonalData = hasPersonalData;
    }

    public String getFullName() {
        return fullName;
    }

    public List<ProductPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ProductPermission> permissions) {
        this.permissions = permissions;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
