package ru.pressstart9.petproject.service;

import org.springframework.data.domain.PageRequest;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.dto.PetDto;
import ru.pressstart9.petproject.dao.PetRepository;
import ru.pressstart9.petproject.domain.Pet;
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

    public void deletePetById(long id) {
        Pet deletePet = getPetById(id);
        deletePet.getFriends().forEach(deletePet::removeFriend);
        if (deletePet.getOwner() != null) {
            deletePet.getOwner().removePet(deletePet);
        }
        petRepository.deleteById(id);
    }

    public List<PetDto> getByParams(String name,
                                    String breed,
                                    List<AvailableColor> colors,
                                    int size, int page) {
        return petRepository.findByParams(name.isEmpty() ? null : name,
                        breed.isEmpty() ? null : breed,
                        colors.isEmpty() ? null : colors,
                        PageRequest.of(page, size))
                            .stream().map(PetService::convertToDto).toList();
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
                .ownerId(pet.getOwner() != null ? pet.getOwner().getId() : null)
                .friendsIds(pet.getFriends().stream().map(Pet::getId).collect(Collectors.toSet()))
                .build();
    }
}
