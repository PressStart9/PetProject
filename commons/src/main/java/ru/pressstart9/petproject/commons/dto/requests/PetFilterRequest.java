package ru.pressstart9.petproject.commons.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetFilterRequest {
    public String name;
    public String breed;
    public List<String> colors;
    public int size;
    public int page;
}
