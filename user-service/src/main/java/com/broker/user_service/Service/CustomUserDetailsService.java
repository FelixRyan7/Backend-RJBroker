package com.broker.user_service.Service;

import com.broker.user_service.model.User;
import com.broker.user_service.model.UserPermission;
import com.broker.user_service.repository.UserPermissionRepository;
import com.broker.user_service.repository.UserRepository;
import com.broker.user_service.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<UserPermission> permissions = userPermissionRepository.findByUserId(user.getId());


        // Devolver CustomUserDetails en lugar de User
        return new CustomUserDetails(
                user, permissions
        );
    }
}