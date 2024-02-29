package com.postech.fase5.Fase_5_Ecommerce_Login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityAdapter {

    @Bean
    public DynamoDBUserDetailsService dynamoDBUserDetailsService() {
        return new DynamoDBUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(HttpMethod.POST, "/usuarios/criarUsuarioAdmin").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/usuarios/criarUsuario").permitAll()
                .requestMatchers(HttpMethod.PUT, "/usuarios/atualizarUsuarioAdmin").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin(formLogin ->
                        formLogin
                                .permitAll()
                                .defaultSuccessUrl("/generate-jwt", true)
                )
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()) // Desabilita CSRF
                .userDetailsService(dynamoDBUserDetailsService())
                .sessionManagement(sess -> {
                    sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    sess.maximumSessions(1);
                })
                .rememberMe(remember -> {
                    remember.key("seeddotoken");
                    remember.tokenValiditySeconds(10);
                });

        return http.build();
    }
}
