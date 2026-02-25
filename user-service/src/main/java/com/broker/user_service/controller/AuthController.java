package com.broker.user_service.controller;

import com.broker.user_service.DTOs.JwtResponse;
import com.broker.user_service.DTOs.LoginRequest;
import com.broker.user_service.DTOs.RegisterRequest;
import com.broker.user_service.DTOs.UserDTO;
import com.broker.user_service.Service.UserService;
import com.broker.user_service.model.User;
import com.broker.user_service.model.UserPermission;
import com.broker.user_service.model.UserProfile;
import com.broker.user_service.repository.UserPermissionRepository;
import com.broker.user_service.repository.UserProfileRepository;
import com.broker.user_service.repository.UserRepository;
import com.broker.user_service.security.JwtTokenProvider;
import com.broker.user_service.utils.ProductPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserPermissionRepository userPermissionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            UserDTO newUser = userService.register(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado correctamente!");
            response.put("user", newUser);
            return ResponseEntity.ok(response); // Devuelve el DTO + mensaje a mostrar al frontend
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse); // devuelve mensaje de error al frontend
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            String username = jwtTokenProvider.getUsernameFromToken(token);
            Optional<User> user = userRepository.findByUsername(username);
            Boolean hasPersonalData  = user.get().isHasPersonalData();
            List<UserPermission> userPermissions = userPermissionRepository.findByUserId(userId);
            List<ProductPermission> permissions = userPermissions.stream()
                    .map(UserPermission::getPermission)
                    .toList();

            UserProfile profile = userProfileRepository.findByUserId(userId)
                    .orElse(null); // si aún no completó profile
            String fullName = profile != null ? profile.getFullName() : null;
            return ResponseEntity.ok(new JwtResponse(token, username, userId, hasPersonalData, fullName, permissions));

        } catch (AuthenticationException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario o contraseña incorrecta");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

}
