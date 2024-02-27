package com.postech.fase5.Fase_5_Ecommerce_Login.controller;

import com.postech.fase5.Fase_5_Ecommerce_Login.repository.UserRepository;
import com.postech.fase5.Fase_5_Ecommerce_Login.service.JwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
public class AuthController {


    @Autowired
    private JwtTokenService jwtTokenService;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @GetMapping("/generate-jwt")
    public ResponseEntity<String> generateJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .compact());
    }
}
