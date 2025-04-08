package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode
public class PersonDto {
    private Long id;
    private String name;
    private Date birthdate;
    private Set<Long> petsIds;
}
