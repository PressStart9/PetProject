package ru.pressstart9.petproject.dto.auth;

import lombok.Data;

@Data
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
}
