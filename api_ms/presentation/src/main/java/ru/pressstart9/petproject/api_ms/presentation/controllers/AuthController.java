package ru.pressstart9.petproject.api_ms.presentation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.pressstart9.petproject.dto.auth.CreateAccountBody;
import ru.pressstart9.petproject.dto.auth.JwtAuthResponse;
import ru.pressstart9.petproject.dto.auth.LoginBody;
import ru.pressstart9.petproject.api_ms.service.AuthService;
import ru.pressstart9.petproject.api_ms.presentation.security.JwtTokenProvider;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtAuthResponse> createPerson(@Valid @RequestBody CreateAccountBody request) throws ExecutionException, InterruptedException {
        var response = authService.createAccount(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                        new JwtAuthResponse(jwtTokenProvider.generateToken(response)));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginBody request) {
        JwtAuthResponse response = new JwtAuthResponse(
            jwtTokenProvider.generateToken(authService.login(request)));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
