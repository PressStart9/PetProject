package ru.pressstart9.petproject.pet_ms.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.commons.dto.responses.PetDto;
import ru.pressstart9.petproject.pet_ms.dao.PetRepository;
import ru.pressstart9.petproject.pet_ms.domain.Pet;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Long createPet(String name, Date birthdate, String breed, AvailableColor color, Long ownerId) {
        Long id = petRepository.save(new Pet(name, birthdate, breed, color, ownerId)).getId();

        return id;
    }

    @Transactional
    public PetDto getPetDtoById(long id) {
        return convertToDto(getPetById(id));
    }

    public void removePetById(long ownerId, long petId) {
        Pet pet = getPetById(petId);
        pet.ownerId = null;
        petRepository.save(pet);
    }

    public void addPetById(long ownerId, long petId) {
        Pet pet = getPetById(petId);
        pet.ownerId = ownerId;
        petRepository.save(pet);
    }

    public List<PetDto> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetService::convertToDto).toList();
    }

    public List<Long> getByOwner(Long ownerId) {
        List<Pet> pets = petRepository.findByOwnerId(ownerId);
        return pets.stream().map(Pet::getId).toList();
    }

    public void removePetOwner(Long ownerId) {
        List<Pet> pets = petRepository.findByOwnerId(ownerId);
        for (var pet : pets) {
            pet.ownerId = null;
            petRepository.save(pet);
        }
    }

    @Transactional
    public void deletePetById(long id) {
        Pet deletePet = getPetById(id);

        deletePet.getFriends().forEach(deletePet::removeFriend);
        petRepository.deleteById(id);
    }

    @Transactional
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

    @Transactional
    public void addFriend(long petId, long friendId) {
        getPetById(petId).addFriend(getPetById(friendId));
    }

    @Transactional
    public void removeFriend(long petId, long friendId) {
        getPetById(petId).removeFriend(getPetById(friendId));
    }

    private Pet getPetById(long id) {
        return petRepository.findById(id).orElseThrow(() -> new EntityNotFound(String.valueOf(id)));
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
