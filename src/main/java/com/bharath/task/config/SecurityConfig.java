package com.bharath.task.config;

import com.bharath.task.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    // Defines the security rules for HTTP requests
 // In SecurityConfig.java

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection for stateless APIs
                .csrf(AbstractHttpConfigurer::disable)
                // Define authorization rules for endpoints
                .authorizeHttpRequests(auth -> auth
                        // Permit all requests to authentication, registration and Swagger endpoints
                        .requestMatchers(
                                "/api/auth/**",
                                "/api-docs/**", // <-- CORRECTED PATH
                                "/v3/api-docs/**", // Let's keep the default too, just in case
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                // Configure session management to be stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Add our custom JWT filter before the standard username/password filter
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean to provide a password encoder for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean to provide the authentication manager, used for the login process
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}