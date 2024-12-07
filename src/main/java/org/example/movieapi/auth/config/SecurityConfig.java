package org.example.movieapi.auth.config;

import lombok.RequiredArgsConstructor;
import org.example.movieapi.auth.service.AuthFilterService;
import org.example.movieapi.auth.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFilterService authFilterService;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain
}
