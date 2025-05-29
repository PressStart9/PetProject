package ru.pressstart9.petproject.commons.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePetBody {
    public CreatePetBody(String name, Date birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }

    public CreatePetBody(String name, Date birthdate, Long ownerId) {
        this.name = name;
        this.birthdate = birthdate;
    }

    @NotBlank
    public String name;
    @Past @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date birthdate;
    public String breed;
    public String color;
    public Long ownerId;
}
