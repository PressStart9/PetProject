package ru.pressstart9.petproject.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.dto.PetDto;
import ru.pressstart9.petproject.presentation.bodies.CreatePetBody;
import ru.pressstart9.petproject.presentation.bodies.FriendPairBody;
import ru.pressstart9.petproject.service.PetService;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<Long> createPet(@Valid @RequestBody CreatePetBody createPetRequest) {
        Long id = petService.createPet(createPetRequest.getName(),
                createPetRequest.getBirthdate(),
                createPetRequest.getBreed(),
                AvailableColor.valueOf(createPetRequest.getColor()));
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> readPet(@PathVariable("id") Long id) {
        return ResponseEntity.ok(petService.getPetDtoById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable("id") Long id) {
        petService.deletePetById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PetDto>> getFilteredPets(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "breed", defaultValue = "") String breed,
            @RequestParam(name = "colors", defaultValue = "") List<String> colors,
            @RequestParam(name = "size", defaultValue = "5") @Positive int size,
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page) {
        List<AvailableColor> parsedColors = colors.stream()
                .filter(c -> !c.isBlank())
                .map(String::toUpperCase)
                .map(AvailableColor::valueOf)
                .toList();

        return ResponseEntity.ok(petService.getByParams(name, breed, parsedColors, size, page));
    }

    @PutMapping("/friends")
    public ResponseEntity<Void> addFriend(@Valid @RequestBody FriendPairBody request) {
        petService.addFriend(request.getPetId(), request.getFriendId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/friends")
    public ResponseEntity<Void> removeFriend(@Valid @RequestBody FriendPairBody request) {
        petService.removeFriend(request.getPetId(), request.getFriendId());
        return ResponseEntity.noContent().build();
    }
}
