package ru.pressstart9.petproject.presentation.bodies;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class FriendPairBody {
    @NotNull
    public Long petId;
    @NotNull
    public Long friendId;
}
