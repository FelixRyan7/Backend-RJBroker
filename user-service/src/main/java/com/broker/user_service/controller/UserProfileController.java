package com.broker.user_service.controller;

import com.broker.user_service.DTOs.UserProfileRequest;
import com.broker.user_service.DTOs.UserProfileResponse;
import com.broker.user_service.Service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user-profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> createProfile (@RequestBody UserProfileRequest request) {
        return ResponseEntity.ok(
                userProfileService.createProfile(request)
        );
    }

}
