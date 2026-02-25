package com.broker.user_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    // Clave secreta para firmar el JWT
    @Value("${jwt.secret}")
    private String jwtSecret;  // Cambiar esto por una variable de entorno o archivo de configuración

    // Tiempo de expiración del JWT (1 hora)
    private final long jwtExpirationMs = 6000000; // 20 minutos

    // Método para generar el token
    public String generateToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        String username = authentication.getName();

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        List<String> permissions = userPrincipal.getPermissions();

        // Construir el token con los datos necesarios
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles)// Nombre de usuario como "subject
                .claim("permissions", permissions)
                .setIssuedAt(new Date())  // Fecha de emisión
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))  // Fecha de expiración
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())  // Firma con algoritmo HS512
                .compact();
    }

    // Método para validar el token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token);  // Verificar la firma y la validez
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;  // Si el token es inválido o expiró
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    // Método para obtener el nombre de usuario desde el token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Extrae el "subject" del token, que es el nombre de usuario
    }
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class);  // Extrae el claim "roles" y lo convierte a lista de strings
    }

    public List<String> getPermissionsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("permissions", List.class);
    }
}