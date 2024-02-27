package com.postech.fase5.Fase_5_Ecommerce_Carrinho.config;

import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.ApplicationProperties;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.repository.UserRepository;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.service.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityAdapter {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ApplicationProperties applicationProperties;

    @Primary
    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.addFilterAt(new JwtAuthenticationService(userRepository, applicationProperties), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .build();
    }
}
