package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.entities.AvailableColor;

import java.util.Set;

@Data
@Builder
public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private AvailableColor color;
    private Long ownerId;
    private Set<Long> friendsIds;
}
