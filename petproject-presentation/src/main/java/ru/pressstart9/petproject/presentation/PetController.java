package ru.pressstart9.petproject.presentation;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.dto.PetDto;
import ru.pressstart9.petproject.presentation.bodies.CreatePetBody;
import ru.pressstart9.petproject.service.PetService;

import java.util.List;

@RequestMapping("/pets")
@RestController
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public Long createPet(@RequestBody CreatePetBody createPetRequest, BindingResult result) {
        return petService.createPet(createPetRequest.getName(),
                createPetRequest.getBirthdate(),
                createPetRequest.getBreed(),
                AvailableColor.valueOf(createPetRequest.getColor()));
    }

    @GetMapping("/{id}")
    public PetDto readPet(@PathVariable("id") Long petId) {
        return petService.getPetDtoById(petId);
    }
    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable("id") Long petId) {
        petService.deletePetById(petId);
    }

    @GetMapping
    public List<PetDto> getFilteredPets(@RequestParam(name = "name", defaultValue = "") String name,
                                        @RequestParam(name = "breed", defaultValue = "") String breed,
                                        @RequestParam(name = "colors", defaultValue = "") List<String> colors,
                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                        @RequestParam(name = "page", defaultValue = "0") int page) {
        return petService.getByParams(name, breed,
                colors.stream().map(AvailableColor::valueOf).toList(),
                size, page);
    }

    @PostMapping("/friends/{id}/{friendId}")
    public void addPetFriend(@PathVariable("id") Long petId, @PathVariable("friendId") Long petFriendId) {
        petService.addFriend(petId, petFriendId);
    }

    @DeleteMapping("/friends/{id}/{friendId}")
    public void removePetFriend(@PathVariable("id") Long petId, @PathVariable("friendId") Long petFriendId) {
        petService.removeFriend(petId, petFriendId);
    }
}
