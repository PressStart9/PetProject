package ru.pressstart9.petproject.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.Set;

@Data
@Builder
public class PersonDto {
    private Long id;
    private String name;
    private Date birthdate;
    private Set<Long> petsIds;
}
