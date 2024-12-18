package com.example.sponsors.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users", "/api/users/register", "/api/users/send-validation-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register","/api/users","/api/users/send-validation-token").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/users/{id}").hasRole("USER")
                .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("admin")
                .password("{noop}senha123")  // {noop} indica que a senha não está criptografada
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
