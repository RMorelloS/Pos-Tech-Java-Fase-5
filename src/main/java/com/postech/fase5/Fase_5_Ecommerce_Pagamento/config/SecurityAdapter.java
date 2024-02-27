package com.postech.fase5.Fase_5_Ecommerce_Pagamento.config;

import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.ApplicationProperties;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.repository.UserRepository;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.service.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
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

    @Autowired
    private Environment env;
    @Primary
    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.addFilterAt(new JwtAuthenticationService(userRepository, applicationProperties), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .requestMatchers(HttpMethod.GET, "/formaPagamento").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/formaPagamento").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/formaPagamento").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/formaPagamento").hasAnyAuthority("ADMIN", "USER")
                .anyRequest().authenticated()
                .and()
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .build();
    }
}
