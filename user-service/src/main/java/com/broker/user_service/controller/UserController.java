package com.broker.user_service.controller;

import com.broker.user_service.DTOs.LoginRequest;
import com.broker.user_service.DTOs.MeResponse;
import com.broker.user_service.DTOs.UserBasicInfo;
import com.broker.user_service.Service.UserPermissionService;
import com.broker.user_service.Service.UserProfileService;
import com.broker.user_service.Service.UserService;
import com.broker.user_service.repository.UserPermissionRepository;
import com.broker.user_service.repository.UserProfileRepository;
import com.broker.user_service.repository.UserRepository;
import com.broker.user_service.utils.ProductPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/me")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserPermissionService userPermissionService;
    @Autowired
    private UserProfileService userProfileService;


    @GetMapping
    public MeResponse me() {
        UserBasicInfo userBasicinfo = userService.getFreshData();
        List<ProductPermission> permissions = userPermissionService.getPermissions();
        String fullName = userProfileService.getFullNameSafe();

        MeResponse response = new MeResponse(userBasicinfo.getUserId(), userBasicinfo.getUsername(),fullName, permissions, userBasicinfo.isHasPersonalData());
        return response;
    }
}
