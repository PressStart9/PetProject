package ru.pressstart9.petproject.commons.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pressstart9.petproject.commons.AvailableColor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetDto {
    private Long id;
    private String name;
    private Date birthdate;
    private String breed;
    private AvailableColor color;
    private Long ownerId;
    private Set<Long> friendsIds;
}
