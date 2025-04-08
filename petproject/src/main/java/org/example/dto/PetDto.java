package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@Builder
@EqualsAndHashCode
public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private AvailableColor color;
    private Long ownerId;
    private Set<Long> friendsIds;
}
