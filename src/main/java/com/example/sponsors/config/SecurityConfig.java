package com.example.sponsors.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private SecurityFilter securityFilter; // Filtro para validação do token

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuração CORS
                .csrf(csrf -> csrf.disable()) // Desabilitando CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sessão stateless
                .authorizeHttpRequests(authorize -> authorize
                        // Rotas públicas (não exigem autenticação)
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Rotas protegidas (exigem autenticação)
                        .requestMatchers(HttpMethod.GET, "/user").authenticated()
                        .requestMatchers(HttpMethod.GET, "/user/all").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/user/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/events/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/events/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/events/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/events/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/locations/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/locations/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/locations/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/locations/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/sponsors/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/sponsors/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/sponsors/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/sponsors/**").authenticated()

                        // Qualquer outra rota requer autenticação
                        .anyRequest().authenticated()
                )
                // Adicionando o filtro de validação do token antes do filtro padrão do Spring Security
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Configurar conforme necessário
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
