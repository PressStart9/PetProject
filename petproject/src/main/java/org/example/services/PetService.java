package org.example.services;

import lombok.AllArgsConstructor;
import org.example.DaoFactory;
import org.example.dto.PetDto;
import org.example.entities.AvailableColor;
import org.example.entities.Pet;
import org.example.repositories.PetRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PetService {
    private final DaoFactory daoFactory;
    private final PetRepository petRepository;

    public List<PetDto> getPetsByColor(AvailableColor color) {
        return petRepository.getPetsByColor(color).stream().map(this::convertToDto).toList();
    }

    public Long createPet(PetDto petDto) {
        return petRepository.save(convertToEntity(petDto)).getId();
    }

    public PetDto getPetById(long id) {
        Pet pet = petRepository.getById(id);
        return pet != null ? convertToDto(pet) : null;
    }

    public List<PetDto> getAllPets() {
        List<Pet> pets = petRepository.getAll();
        return pets.stream().map(this::convertToDto).toList();
    }

    public void updatePet(PetDto petDto) {
        petRepository.update(convertToEntity(petDto));
    }

    public void deletePetById(long id) {
        petRepository.deleteById(id);
    }

    public void deleteAllPets() {
        petRepository.deleteAll();
    }

    private PetDto convertToDto(Pet pet) {
        return daoFactory.inTransaction(entityManager -> {
            entityManager.refresh(pet);
            return PetDto.builder()
                    .id(pet.getId())
                    .name(pet.getName())
                    .breed(pet.getBreed())
                    .color(pet.getColor())
                    .ownerId(pet.getOwner() != null ? pet.getOwner().getId() : null)
                    .friendsIds(pet.getFriends().stream().map(Pet::getId).collect(Collectors.toSet()))
                    .build();
        });
    }

    private Pet convertToEntity(PetDto petDto) {
        Pet pet = new Pet();
        pet.setId(petDto.getId());
        pet.setName(petDto.getName());
        pet.setBreed(petDto.getBreed());
        pet.setColor(petDto.getColor());

        pet.setOwner(petDto.getOwnerId() == null ? null :
                daoFactory.getPersonRepository().getById(petDto.getOwnerId()));
        pet.setFriends(petDto.getFriendsIds() == null ? null :
                new HashSet<>(petRepository.getPetsByIds(petDto.getFriendsIds())));

        return pet;
    }
}
