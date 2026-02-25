package com.broker.user_service.Service;

import com.broker.user_service.DTOs.UserProfileRequest;
import com.broker.user_service.DTOs.UserProfileResponse;
import com.broker.user_service.model.UserPermission;
import com.broker.user_service.repository.UserPermissionRepository;
import com.broker.user_service.utils.CurrentUserProvider;
import com.broker.user_service.utils.ProductPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPermissionService {

    @Autowired
    private UserPermissionRepository userPermissionRepository;
    @Autowired
    private CurrentUserProvider currentUserProvider;

    public List<ProductPermission> getPermissions() {
        Long userId = currentUserProvider.getUserId();
        List<UserPermission> userPermissions = userPermissionRepository.findByUserId(userId);
        List<ProductPermission> permissions = userPermissions.stream()
                .map(UserPermission::getPermission)
                .toList();
        return (permissions);
    }
}
