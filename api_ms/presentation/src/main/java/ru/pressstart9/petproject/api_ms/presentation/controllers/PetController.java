//package ru.pressstart9.petproject.api_ms.presentation.controllers;
//
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Positive;
//import jakarta.validation.constraints.PositiveOrZero;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.parameters.P;
//import org.springframework.web.bind.annotation.*;
//import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
//import ru.pressstart9.petproject.common_kafka.AvailableColor;
//import ru.pressstart9.petproject.dto.requests.GetRequest;
//import ru.pressstart9.petproject.dto.PetDto;
//import ru.pressstart9.petproject.api_ms.dto.requests.CreatePetBody;
//import ru.pressstart9.petproject.api_ms.dto.requests.FriendPairBody;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//@RequestMapping("/pets")
//public class PetController {
//    private final RequestProducer requestProducer;
//
//    public PetController(RequestProducer requestProducer) {
//        this.requestProducer = requestProducer;
//    }
//
//    @PostMapping
//    public CompletableFuture<ResponseEntity<Long>> createPet(@Valid @RequestBody CreatePetBody request) {
//        Long id = petService.createPet(request.getName(),
//                request.getBirthdate(),
//                request.getBreed(),
//                request.getColor() == null ? null : AvailableColor.valueOf(request.getColor()));
//        return ResponseEntity.status(HttpStatus.CREATED).body(id);
//    }
//
//    @GetMapping("/{id}")
//    public CompletableFuture<ResponseEntity<PetDto>> readPet(@PathVariable("id") Long id) {
//        return requestProducer.sendPetRequest(new GetRequest(id)).thenApply(ResponseEntity::ok);
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("@permission.isOwner(#id)")
//    public CompletableFuture<ResponseEntity<Void>> deletePet(@P("id") @PathVariable("id") Long id) {
//        petService.deletePetById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping
//    public CompletableFuture<ResponseEntity<List<PetDto>>> getFilteredPets(
//            @RequestParam(name = "name", defaultValue = "") String name,
//            @RequestParam(name = "breed", defaultValue = "") String breed,
//            @RequestParam(name = "colors", defaultValue = "") List<String> colors,
//            @RequestParam(name = "size", defaultValue = "5") @Positive int size,
//            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page) {
//        List<AvailableColor> parsedColors = colors.stream()
//                .filter(c -> !c.isBlank())
//                .map(String::toUpperCase)
//                .map(AvailableColor::valueOf)
//                .toList();
//
//        return ResponseEntity.ok(petService.getByParams(name, breed, parsedColors, size, page));
//    }
//
//    @PutMapping("/friends")
//    @PreAuthorize("@permission.isOwnerOfPair(#request)")
//    public CompletableFuture<ResponseEntity<Void>> addFriend(@P("request") @Valid @RequestBody FriendPairBody request) {
//        petService.addFriend(request.getPetId(), request.getFriendId());
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping("/friends")
//    @PreAuthorize("@permission.isOwnerOfPair(#request)")
//    public CompletableFuture<ResponseEntity<Void>> removeFriend(@P("request") @Valid @RequestBody FriendPairBody request) {
//        petService.removeFriend(request.getPetId(), request.getFriendId());
//        return ResponseEntity.noContent().build();
//    }
//}
