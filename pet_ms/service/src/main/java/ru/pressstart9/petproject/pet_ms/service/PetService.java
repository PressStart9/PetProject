package ru.pressstart9.petproject.pet_ms.service;

import org.springframework.data.domain.PageRequest;
import ru.pressstart9.petproject.common_kafka.AvailableColor;
import ru.pressstart9.petproject.common_kafka.exceptions.EntityNotFound;
import ru.pressstart9.petproject.dto.PetDto;
import ru.pressstart9.petproject.dto.requests.RemovePetRequest;
import ru.pressstart9.petproject.pet_ms.dao.PetRepository;
import ru.pressstart9.petproject.pet_ms.domain.Pet;
import org.springframework.stereotype.Service;
import ru.pressstart9.petproject.pet_ms.service.kafka.RequestProducer;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final RequestProducer requestProducer;

    public PetService(PetRepository petRepository, RequestProducer requestProducer) {
        this.petRepository = petRepository;
        this.requestProducer = requestProducer;
    }

    public Long createPet(String name, Date birthdate, String breed, AvailableColor color) {
        return petRepository.save(new Pet(name, birthdate, breed, color)).getId();
    }

    public PetDto getPetDtoById(long id) {
        return convertToDto(getPetById(id));
    }

    public List<PetDto> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetService::convertToDto).toList();
    }

    public CompletableFuture<Void> deletePetById(long id) {
        Pet deletePet = getPetById(id);

        return requestProducer.sendPersonRequest(new RemovePetRequest(deletePet.getOwnerId(), id))
            .thenRun(() -> {
                    deletePet.getFriends().forEach(deletePet::removeFriend);
                    petRepository.deleteById(id);
                }
            );
    }

    public List<PetDto> getByParams(String name, String breed,
                                    List<AvailableColor> colors, int size, int page) {
        return petRepository.findByParams(
                        name.isBlank() ? null : name,
                        breed.isBlank() ? null : breed,
                        colors == null || colors.isEmpty() ? null : colors,
                        PageRequest.of(page, size))
                .stream()
                .map(PetService::convertToDto)
                .toList();
    }

    public void addFriend(long petId, long friendId) {
        getPetById(petId).addFriend(getPetById(friendId));
    }

    public void removeFriend(long petId, long friendId) {
        getPetById(petId).removeFriend(getPetById(friendId));
    }

    private Pet getPetById(long id) {
        return petRepository.findById(id).orElseThrow(() -> new EntityNotFound(id));
    }

    public static PetDto convertToDto(Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .birthdate(pet.getBirthdate())
                .breed(pet.getBreed())
                .color(pet.getColor())
                .ownerId(pet.getOwnerId())
                .friendsIds(pet.getFriends().stream().map(Pet::getId).collect(Collectors.toSet()))
                .build();
    }
}
