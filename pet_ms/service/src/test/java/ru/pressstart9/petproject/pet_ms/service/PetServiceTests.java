package ru.pressstart9.petproject.pet_ms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.pet_ms.dao.PetRepository;
import ru.pressstart9.petproject.pet_ms.domain.Pet;
import ru.pressstart9.petproject.commons.dto.responses.PetDto;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PetServiceTests {
    @Mock
    private PetRepository petRepo;

    @InjectMocks
    private PetService petServ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePet() {
        Pet pet = new Pet("Barsik",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);
        pet.setId(1L);

        PetDto petDto = PetService.convertToDto(pet);

        when(petRepo.save(any(Pet.class))).thenReturn(pet);
        when(petRepo.findById(1L)).thenReturn(Optional.of(pet));

        Long id = petServ.createPet(petDto.getName(), petDto.getBirthdate(), petDto.getBreed(), petDto.getColor(), null);

        PetDto createdPet = petServ.getPetDtoById(id);
        assertNotNull(createdPet);
        assertEquals(pet.getName(), createdPet.getName());
    }

    @Test
    void testGetAllPets() {
        Pet pet1 = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);
        pet1.setId(1L);

        Pet pet2 = new Pet("Pet2",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);
        pet2.setId(2L);

        when(petRepo.findAll()).thenReturn(List.of(pet1, pet2));

        List<PetDto> allPets = petServ.getAllPets();
        assertEquals(2, allPets.size());
    }

    @Test
    void testGetPetDtoById() {
        Pet pet = new Pet("Barsik",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);
        pet.setId(1L);

        when(petRepo.findById(1L)).thenReturn(Optional.of(pet));

        PetDto petDto = petServ.getPetDtoById(1L);
        assertNotNull(petDto);
        assertEquals(pet.getName(), petDto.getName());
    }

    @Test
    void testDeletePetById() {
        Pet pet = new Pet("Barsik",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);
        pet.setId(1L);

        when(petRepo.findById(1L)).thenReturn(Optional.of(pet));

        petServ.deletePetById(1L);
        verify(petRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePetByIdNotFound() {
        when(petRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFound.class, () -> petServ.deletePetById(1L));
    }

    @Test
    void testGetByParams() {
        Pet pet1 = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);
        pet1.setId(1L);

        Pet pet2 = new Pet("Pet2",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.white);
        pet2.setId(2L);

        when(petRepo.findByParams(isNull(), isNull(), isNull(), any(PageRequest.class)))
                .thenReturn(List.of(pet1, pet2));

        List<PetDto> pets = petServ.getByParams("", "", List.of(), 10, 0);
        assertEquals(2, pets.size());
    }

    @Test
    void testAddFriend() {
        Pet pet1 = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);
        pet1.setId(1L);

        Pet pet2 = new Pet("Pet2",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.white);
        pet2.setId(2L);

        when(petRepo.findById(1L)).thenReturn(Optional.of(pet1));
        when(petRepo.findById(2L)).thenReturn(Optional.of(pet2));

        petServ.addFriend(1L, 2L);
        assertTrue(pet1.getFriends().contains(pet2));
        assertTrue(pet2.getFriends().contains(pet1));
    }

    @Test
    void testRemoveFriend() {
        Pet pet1 = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);
        pet1.setId(1L);

        Pet pet2 = new Pet("Pet2",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.white);
        pet2.setId(2L);

        pet1.addFriend(pet2);

        when(petRepo.findById(1L)).thenReturn(Optional.of(pet1));
        when(petRepo.findById(2L)).thenReturn(Optional.of(pet2));

        petServ.removeFriend(1L, 2L);
        assertIterableEquals(List.of(), pet1.getFriends());
        assertIterableEquals(List.of(), pet2.getFriends());
    }
}
