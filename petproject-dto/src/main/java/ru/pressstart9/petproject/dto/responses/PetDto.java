package ru.pressstart9.petproject.dto.responses;

import lombok.Builder;
import lombok.Data;
import ru.pressstart9.petproject.commons.AvailableColor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class PetDto {
    private Long id;
    private String name;
    private Date birthdate;
    private String breed;
    private AvailableColor color;
    private Long ownerId;
    private Set<Long> friendsIds;
}
