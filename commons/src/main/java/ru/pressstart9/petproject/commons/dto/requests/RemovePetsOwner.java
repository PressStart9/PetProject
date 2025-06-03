package ru.pressstart9.petproject.commons.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemovePetsOwner {
    public Long ownerId;
}
