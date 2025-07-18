package ru.pressstart9.petproject.commons.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveFriendPair {
    @NotNull
    public Long petId;
    @NotNull
    public Long friendId;
}
