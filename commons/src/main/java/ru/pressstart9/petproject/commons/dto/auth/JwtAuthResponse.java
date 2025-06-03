package ru.pressstart9.petproject.commons.dto.auth;

import lombok.Data;

@Data
public class JwtAuthResponse {
    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;
    private String tokenType = "Bearer";
}
