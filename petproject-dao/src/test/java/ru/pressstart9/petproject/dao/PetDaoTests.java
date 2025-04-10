package ru.pressstart9.petproject.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.domain.Pet;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PetDaoTests extends BaseTestContainer {
    @BeforeEach
    void setUp() {
        petRepo.deleteAll();
    }

    @Autowired
    PetRepository petRepo;

    @Test
    public void testSaveAndGetPet() {
        Pet pet = new Pet("Barsik",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);

        petRepo.save(pet);

        Pet retrievedPet = petRepo.findById(pet.getId()).orElseThrow();

        assertEquals(pet, retrievedPet);
    }

    @Test
    public void testUpdatePet() {
        Pet pet = new Pet("Barsik",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);

        petRepo.save(pet);

        var newName = "Updated Barsik";
        pet.setName(newName);
        petRepo.save(pet);

        Pet updatedPet = petRepo.findById(pet.getId()).orElseThrow();

        assertEquals(newName, updatedPet.getName());
    }

    @Test
    public void testDeletePet() {
        Pet pet = new Pet("Barsik",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);

        petRepo.save(pet);

        petRepo.deleteById(pet.getId());

        assertTrue(petRepo.findById(pet.getId()).isEmpty());
    }

    @Test
    public void testGetAllPets() {
        Pet pet1 = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);

        Pet pet2 = new Pet("Pet2",
                Date.valueOf("2025-01-01"),
                "Ragdoll",
                AvailableColor.Blue);

        petRepo.save(pet1);
        petRepo.save(pet2);

        List<Pet> pets = petRepo.findAll();

        assertIterableEquals(List.of(pet1, pet2), pets);
    }

    @Test
    public void testAddFriends() {
        Pet pet1 = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);

        Pet pet2 = new Pet("Pet2",
                Date.valueOf("2025-01-01"),
                "Ragdoll",
                AvailableColor.Blue);

        pet1.addFriend(pet2);

        petRepo.save(pet1);
        petRepo.save(pet2);

        assertIterableEquals(List.of(pet1), pet2.getFriends());
        assertIterableEquals(List.of(pet2), pet1.getFriends());
    }

    @Test
    public void testAddYourselfAsFriend() {
        Pet pet1 = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.Black);

        petRepo.save(pet1);

        pet1.addFriend(pet1);

        assertIterableEquals(List.of(), pet1.getFriends());
    }
}
