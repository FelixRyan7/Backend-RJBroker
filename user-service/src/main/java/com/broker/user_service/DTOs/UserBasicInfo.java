package com.broker.user_service.DTOs;

public class UserBasicInfo {
    private Long userId;
    private String username;
    private boolean hasPersonalData;

    public UserBasicInfo(Long userId, String username, boolean hasPersonalData) {
        this.userId = userId;
        this.username = username;
        this.hasPersonalData = hasPersonalData;
    }

    // getters y setters
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public boolean isHasPersonalData() { return hasPersonalData; }
}
