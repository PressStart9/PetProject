package ru.pressstart9.petproject.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class CreateAccountBody {
    @Email
    public String email;
    @Size(min = 8)
    public String password;

    @NotBlank
    public String name;
    @Past @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date birthdate;
}
