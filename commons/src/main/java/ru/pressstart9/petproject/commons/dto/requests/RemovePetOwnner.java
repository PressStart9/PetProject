package ru.pressstart9.petproject.commons.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemovePetOwnner {
    public Long ownerId;
    public Long petId;
}
