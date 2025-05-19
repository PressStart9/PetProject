package ru.pressstart9.petproject.dto.responses;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class PersonDto {
    private Long id;
    private String name;
    private Date birthdate;
    private Set<Long> petsIds;
}
