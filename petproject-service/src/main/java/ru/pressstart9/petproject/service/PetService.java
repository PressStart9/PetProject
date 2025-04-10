package ru.pressstart9.petproject.service;

import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.dto.PetDto;
import ru.pressstart9.petproject.dao.PetRepository;
import ru.pressstart9.petproject.domain.Pet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Long createPet(PetDto petDto) {
        return petRepository.save(
                new Pet(petDto.getName(),
                petDto.getBirthdate(),
                petDto.getBreed(),
                petDto.getColor())).getId();
    }

    public PetDto getPetDtoById(long id) {
        return convertToDto(getPetById(id));
    }

    public List<PetDto> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetService::convertToDto).toList();
    }

    public void deletePetById(long id) {
        Pet deletePet = getPetById(id);
        deletePet.getFriends().forEach(deletePet::removeFriend);
        deletePet.getOwner().removePet(deletePet);
        petRepository.deleteById(id);
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
                .ownerId(pet.getOwner() != null ? pet.getOwner().getId() : null)
                .friendsIds(pet.getFriends().stream().map(Pet::getId).collect(Collectors.toSet()))
                .build();
    }
}
