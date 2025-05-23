package ru.pressstart9.petproject.commons.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class LoginBody {
    @Email @NotNull
    public String email;
    @NotNull
    public String password;
}
