package org.example;

import org.example.dto.PersonDto;
import org.example.dto.PetDto;
import org.example.entities.AvailableColor;
import org.example.repositories.PersonRepository;
import org.example.repositories.PetRepository;
import org.example.services.PersonService;
import org.example.services.PetService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    static DaoFactory daoFactory;

    static PetRepository petRepo;
    static PersonRepository personRepo;

    static PetService petServ;
    static PersonService personServ;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        daoFactory = new DaoFactory(
                postgres.getUsername(),
                postgres.getPassword(),
                postgres.getFirstMappedPort().toString(),
                postgres.getDatabaseName()
        );

        petRepo = daoFactory.getPetRepository();
        personRepo = daoFactory.getPersonRepository();

        petServ = new PetService(daoFactory, petRepo);
        personServ = new PersonService(daoFactory, personRepo);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        petRepo.deleteAll();
        personRepo.deleteAll();
    }

    @Test
    void testCreatePet() {
        PetDto petDto = PetDto.builder()
                .name("Pet1")
                .breed("Breed1")
                .color(AvailableColor.Red)
                .build();

        var id = petServ.createPet(petDto);

        PetDto createdPet = petServ.getPetById(id);
        assertNotNull(createdPet);
        assertEquals("Pet1", createdPet.getName());
    }

    @Test
    void testGetAllPets() {
        PetDto petDto1 = PetDto.builder()
                .name("Pet3")
                .breed("Breed3")
                .color(AvailableColor.Green)
                .build();

        PetDto petDto2 = PetDto.builder()
                .name("Pet4")
                .breed("Breed4")
                .color(AvailableColor.Yellow)
                .build();

        petServ.createPet(petDto1);
        petServ.createPet(petDto2);

        List<PetDto> allPets = petServ.getAllPets();
        assertEquals(2, allPets.size());
    }

    @Test
    void testUpdatePet() {
        PetDto petDto = PetDto.builder()
                .name("Pet5")
                .breed("Breed5")
                .color(AvailableColor.Red)
                .build();

        var id = petServ.createPet(petDto);

        PetDto updatedPetDto = PetDto.builder()
                .id(id)
                .name("UpdatedPet")
                .breed("UpdatedBreed")
                .color(AvailableColor.Blue)
                .build();

        petServ.updatePet(updatedPetDto);

        PetDto updatedPet = petServ.getPetById(id);
        assertEquals("UpdatedPet", updatedPet.getName());
    }

    @Test
    void testDeletePetById() {
        PetDto petDto = PetDto.builder()
                .name("Pet6")
                .breed("Breed6")
                .color(AvailableColor.Red)
                .build();

        var id = petServ.createPet(petDto);

        petServ.deletePetById(id);

        PetDto deletedPet = petServ.getPetById(id);
        assertNull(deletedPet);
        assertTrue(petServ.getAllPets().isEmpty());
    }

    @Test
    void testDeleteAllPets() {
        PetDto petDto1 = PetDto.builder()
                .name("Pet7")
                .breed("Breed7")
                .color(AvailableColor.Red)
                .build();

        PetDto petDto2 = PetDto.builder()
                .name("Pet8")
                .breed("Breed8")
                .color(AvailableColor.Blue)
                .build();

        petServ.createPet(petDto1);
        petServ.createPet(petDto2);

        petServ.deleteAllPets();

        assertTrue(petServ.getAllPets().isEmpty());
    }

    @Test
    void testGetPetsByColor() {
        PetDto petDto1 = PetDto.builder()
                .name("Pet9")
                .breed("Breed9")
                .color(AvailableColor.Red)
                .build();

        PetDto petDto2 = PetDto.builder()
                .name("Pet10")
                .breed("Breed10")
                .color(AvailableColor.Red)
                .build();

        PetDto petDto3 = PetDto.builder()
                .name("Pet8")
                .breed("Breed8")
                .color(AvailableColor.Blue)
                .build();

        petServ.createPet(petDto1);
        petServ.createPet(petDto2);
        petServ.createPet(petDto3);

        List<PetDto> redPets = petServ.getPetsByColor(AvailableColor.Red);
        assertEquals(2, redPets.size());
    }

    @Test
    void testCreatePetWithOwner() {
        PersonDto ownerDto = PersonDto.builder()
                .name("Owner1")
                .birthdate(Date.valueOf("2020-01-01"))
                .build();

        var ownerId = personServ.createPerson(ownerDto);

        PetDto petDto = PetDto.builder()
                .name("Pet11")
                .breed("Breed11")
                .color(AvailableColor.Red)
                .ownerId(ownerId)
                .build();

        var petId = petServ.createPet(petDto);

        PetDto createdPet = petServ.getPetById(petId);
        assertEquals(ownerId, createdPet.getOwnerId());
    }

    @Test
    void testCreatePetWithFriends() {
        PetDto friendDto1 = PetDto.builder()
                .name("Friend1")
                .breed("Breed12")
                .color(AvailableColor.Blue)
                .build();

        PetDto friendDto2 = PetDto.builder()
                .name("Friend2")
                .breed("Breed13")
                .color(AvailableColor.Green)
                .build();

        var friendId1 = petServ.createPet(friendDto1);
        var friendId2 = petServ.createPet(friendDto2);

        PetDto petDto = PetDto.builder()
                .name("Pet12")
                .breed("Breed14")
                .color(AvailableColor.Red)
                .friendsIds(Set.of(friendId1, friendId2))
                .build();

        var petId = petServ.createPet(petDto);

        PetDto createdPet = petServ.getPetById(petId);
        assertEquals(2, createdPet.getFriendsIds().size());
    }

    @Test
    void testUpdatePetWithNewOwner() {
        PersonDto ownerDto = PersonDto.builder()
                .name("Owner2")
                .build();

        var ownerId = personServ.createPerson(ownerDto);

        PetDto petDto = PetDto.builder()
                .name("Pet13")
                .breed("Breed15")
                .color(AvailableColor.Red)
                .build();

        var petId = petServ.createPet(petDto);

        PetDto updatedPetDto = PetDto.builder()
                .id(petId)
                .name("Pet13")
                .breed("Breed15")
                .color(AvailableColor.Red)
                .ownerId(ownerId)
                .build();

        petServ.updatePet(updatedPetDto);

        PetDto updatedPet = petServ.getPetById(petId);
        assertEquals(ownerId, updatedPet.getOwnerId());
    }
}
