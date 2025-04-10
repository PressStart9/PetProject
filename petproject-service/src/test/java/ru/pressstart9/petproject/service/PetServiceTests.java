package ru.pressstart9.petproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.dao.PersonRepository;
import ru.pressstart9.petproject.dao.PetRepository;
import ru.pressstart9.petproject.domain.Pet;
import ru.pressstart9.petproject.dto.PetDto;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class PetServiceTests {
    @Mock
    private PetRepository petRepo;
    @Mock
    private PersonRepository personRepo;

    @InjectMocks
    private PetService petServ;
    @InjectMocks
    private PersonService personServ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePet() {
        Pet pet = new Pet("Barsik",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);
        pet.setId(1L);

        PetDto petDto = PetService.convertToDto(pet);

        when(petRepo.save(any(Pet.class))).thenReturn(pet);
        when(petRepo.findById(1L)).thenReturn(Optional.of(pet));

        Long id = petServ.createPet(petDto);

        PetDto createdPet = petServ.getPetDtoById(id);
        assertNotNull(createdPet);
        assertEquals(pet.getName(), createdPet.getName());
    }

    @Test
    void testGetAllPets() {
        Pet pet1 = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);
        pet1.setId(1L);

        Pet pet2 = new Pet("Pet2",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);
        pet1.setId(2L);

        when(petRepo.findAll()).thenReturn(List.of(pet1, pet2));

        List<PetDto> allPets = petServ.getAllPets();
        assertEquals(2, allPets.size());
    }
}
