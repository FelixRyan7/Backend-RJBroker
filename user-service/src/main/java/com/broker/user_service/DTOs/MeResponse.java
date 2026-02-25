package com.broker.user_service.DTOs;

import com.broker.user_service.utils.ProductPermission;

import java.util.List;

public class MeResponse {
    private Long userId;
    private String username;
    private String fullName;
    private List<ProductPermission> permissions;
    private boolean hasPersonalData;

    public MeResponse(
            Long userId,
            String username,
            String fullName,
            List<ProductPermission> permissions,
            boolean hasPersonalData
    ) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.permissions = permissions;
        this.hasPersonalData = hasPersonalData;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<ProductPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ProductPermission> permissions) {
        this.permissions = permissions;
    }

    public boolean isHasPersonalData() {
        return hasPersonalData;
    }

    public void setHasPersonalData(boolean hasPersonalData) {
        this.hasPersonalData = hasPersonalData;
    }
}
