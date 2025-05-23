package ru.pressstart9.petproject.api_ms.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.commons.dto.requests.*;
import ru.pressstart9.petproject.commons.dto.responses.CreatedResponse;
import ru.pressstart9.petproject.commons.dto.responses.PetDto;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {
    private final RequestProducer requestProducer;

    public PetController(RequestProducer requestProducer) {
        this.requestProducer = requestProducer;
    }

    @PostMapping
    public ResponseEntity<Long> createPet(@Valid @RequestBody CreatePetBody request) {
        CreatedResponse response = requestProducer.sendPetRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> readPet(@PathVariable("id") Long id) {
        PetDto response = requestProducer.sendPetRequest(new GetRequest(id));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.isOwner(#id)")
    public ResponseEntity<Void> deletePet(@P("id") @PathVariable("id") Long id) {
        requestProducer.sendPetRequest(new DeleteRequest(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PetDto>> getFilteredPets(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "breed", defaultValue = "") String breed,
            @RequestParam(name = "colors", defaultValue = "") List<String> colors,
            @RequestParam(name = "size", defaultValue = "5") @Positive int size,
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page) {
        List<PetDto> result = requestProducer.sendPetRequest(new PetFilterRequest(name, breed, colors, size, page));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/friends")
    @PreAuthorize("@permission.isOwnerOfPair(#request)")
    public ResponseEntity<Void> addFriend(@P("request") @Valid @RequestBody FriendPairBody request) {
        requestProducer.sendPetRequest(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/friends")
    @PreAuthorize("@permission.isOwnerOfPair(#request)")
    public ResponseEntity<Void> removeFriend(@P("request") @Valid @RequestBody RemoveFriendPair request) {
        requestProducer.sendPetRequest(request);
        return ResponseEntity.noContent().build();
    }
}
