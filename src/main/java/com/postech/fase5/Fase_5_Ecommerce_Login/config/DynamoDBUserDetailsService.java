package com.postech.fase5.Fase_5_Ecommerce_Login.config;

import com.postech.fase5.Fase_5_Ecommerce_Login.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.postech.fase5.Fase_5_Ecommerce_Login.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Primary
@NoArgsConstructor
@AllArgsConstructor
@Service
public class DynamoDBUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.getUserByLogin(username).get();
            return User
                    .withUsername(user.getUsername())
                    .password(passwordEncoder().encode(user.getPassword()))
                    .authorities(user.getAuthorities())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
