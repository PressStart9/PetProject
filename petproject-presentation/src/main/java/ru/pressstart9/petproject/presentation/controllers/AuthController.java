package ru.pressstart9.petproject.presentation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.pressstart9.petproject.dto.auth.CreateAccountBody;
import ru.pressstart9.petproject.dto.auth.JwtAuthResponse;
import ru.pressstart9.petproject.dto.auth.LoginBody;
import ru.pressstart9.petproject.service.AuthService;
import ru.pressstart9.petproject.presentation.security.JwtTokenProvider;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtAuthResponse> createPerson(@Valid @RequestBody CreateAccountBody request) {
        JwtAuthResponse response = new JwtAuthResponse();
        response.setAccessToken(jwtTokenProvider.generateToken(authService.createAccount(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginBody request) {
        JwtAuthResponse response = new JwtAuthResponse();
        response.setAccessToken(jwtTokenProvider.generateToken(authService.login(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
