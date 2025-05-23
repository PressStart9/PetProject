package ru.pressstart9.petproject.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemovePetRequest {
    public Long ownerId;
    public Long petId;
}
