package ru.pressstart9.petproject.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class CreatePersonBody {
    @NotBlank
    public String name;
    @Past
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date birthdate;
}
