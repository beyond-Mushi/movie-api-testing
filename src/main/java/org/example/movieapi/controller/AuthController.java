package org.example.movieapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.movieapi.auth.entity.RefreshToken;
import org.example.movieapi.auth.entity.User;
import org.example.movieapi.auth.service.AuthService;
import org.example.movieapi.auth.service.RefreshTokenService;
import org.example.movieapi.auth.utils.AuthResponse;
import org.example.movieapi.auth.utils.LoginRequest;
import org.example.movieapi.auth.utils.RefreshTokenRequest;
import org.example.movieapi.auth.utils.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) throws InstantiationException, IllegalAccessException {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest loginRequest) throws InstantiationException, IllegalAccessException {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService
                .verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        return null;
    }

}
