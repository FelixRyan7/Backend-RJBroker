package com.broker.user_service.DTOs;

import com.broker.user_service.model.UserProfile;

public class UserProfileResponse {

    private Long id;
    private String fullName;


    public UserProfileResponse(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
