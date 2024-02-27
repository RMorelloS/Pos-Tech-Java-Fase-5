package com.postech.fase5.Fase_5_Ecommerce_Carrinho.service;

import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.ApplicationProperties;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class JwtAuthenticationService extends OncePerRequestFilter {



    public JwtAuthenticationService(UserRepository userRepository, ApplicationProperties applicationProperties){
        this.userRepository = userRepository;
        this.jwtSecret = applicationProperties.getJwtSecret();
    }
    private String jwtSecret;

    @Autowired
    private UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwtToken = extractJwtToken(request);
            if (jwtToken != null && validateToken(jwtToken)) {
                String username = extractUsername(jwtToken);

                var user = userRepository.getUserByLogin(username);
                if(user != null) {

                    UserDetails userDetails = user.get();
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    throw new UsernameNotFoundException("Usuario " + username + " não encontrado");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);

    }



    private String extractJwtToken(HttpServletRequest request) throws IOException {
        String token = request.getHeader("token");
        if (token != null && !token.isEmpty()) {
            return token;
        }
        return "";
    }

    private boolean validateToken(String jwtToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private String extractUsername(String jwtToken) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
        return claims.getSubject();
    }
}
