package com.broker.user_service.security;

import com.broker.user_service.model.User;
import com.broker.user_service.model.UserPermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class CustomUserDetails implements UserDetails {

    private final User user;
    private final List<String> permissions;

    public CustomUserDetails(User user, List<UserPermission> userPermissions) {
        this.user = user;
        this.permissions = userPermissions.stream()
                .map(up -> up.getPermission().name())
                .collect(Collectors.toList());
    }


    public Long getId() {
        return user.getId();
    }

    public List<String> getPermissions() {
        return permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(() -> "USER");
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
