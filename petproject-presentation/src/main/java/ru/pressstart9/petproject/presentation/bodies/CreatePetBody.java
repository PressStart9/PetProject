package ru.pressstart9.petproject.presentation.bodies;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CreatePetBody {
    public String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date birthdate;
    public String breed;
    public String color;
}
