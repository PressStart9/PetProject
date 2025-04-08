package org.example;

import org.example.dao.DaoFactory;
import org.example.dto.PersonDto;
import org.example.dto.PetDto;
import org.example.dto.AvailableColor;
import org.example.dao.PersonRepository;
import org.example.dao.PetRepository;
import org.example.entities.Pet;
import org.example.entities.Person;
import org.example.service.PersonService;
import org.example.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceTests {
    @Mock
    private DaoFactory daoFactory;
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
        when(daoFactory.getPersonRepository()).thenReturn(personRepo);
        when(daoFactory.getPetRepository()).thenReturn(petRepo);
    }

    @Test
    void testCreatePet() {
        PetDto petDto = PetDto.builder()
                .name("Pet1")
                .breed("Breed1")
                .color(AvailableColor.Red)
                .build();

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Pet1");
        pet.setBreed("Breed1");
        pet.setColor(AvailableColor.Red);

        when(petRepo.save(any(Pet.class))).thenReturn(pet);
        when(petRepo.getById(1L)).thenReturn(pet);

        Long id = petServ.createPet(petDto);

        PetDto createdPet = petServ.getPetById(id);
        assertNotNull(createdPet);
        assertEquals(pet.getName(), createdPet.getName());
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

        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Pet3");
        pet1.setBreed("Breed3");
        pet1.setColor(AvailableColor.Green);

        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Pet4");
        pet2.setBreed("Breed4");
        pet2.setColor(AvailableColor.Yellow);

        when(petRepo.getAll()).thenReturn(List.of(pet1, pet2));

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

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Pet5");
        pet.setBreed("Breed5");
        pet.setColor(AvailableColor.Red);

        when(petRepo.save(any(Pet.class))).thenReturn(pet);
        when(petRepo.getById(1L)).thenReturn(pet);

        Long id = petServ.createPet(petDto);

        PetDto updatedPetDto = PetDto.builder()
                .id(id)
                .name("UpdatedPet")
                .breed("UpdatedBreed")
                .color(AvailableColor.Blue)
                .build();

        petServ.updatePet(updatedPetDto);

        pet.setName(updatedPetDto.getName());
        pet.setBreed(updatedPetDto.getBreed());
        pet.setColor(updatedPetDto.getColor());

        PetDto updatedPet = petServ.getPetById(id);
        assertEquals(updatedPetDto.getName(), updatedPet.getName());
    }

    @Test
    void testDeletePetById() {
        PetDto petDto = PetDto.builder()
                .name("Pet6")
                .breed("Breed6")
                .color(AvailableColor.Red)
                .build();

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Pet6");
        pet.setBreed("Breed6");
        pet.setColor(AvailableColor.Red);

        when(petRepo.save(any(Pet.class))).thenReturn(pet);
        when(petRepo.getById(1L)).thenReturn(pet);

        Long id = petServ.createPet(petDto);

        petServ.deletePetById(id);

        when(petRepo.getById(1L)).thenReturn(null);
        when(petRepo.getAll()).thenReturn(List.of());

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

        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Pet7");
        pet1.setBreed("Breed7");
        pet1.setColor(AvailableColor.Red);

        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Pet8");
        pet2.setBreed("Breed8");
        pet2.setColor(AvailableColor.Blue);

        when(petRepo.save(any(Pet.class))).thenReturn(pet1, pet2);
        when(petRepo.getAll()).thenReturn(List.of());

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

        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Pet9");
        pet1.setBreed("Breed9");
        pet1.setColor(AvailableColor.Red);

        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Pet10");
        pet2.setBreed("Breed10");
        pet2.setColor(AvailableColor.Red);

        when(petRepo.save(any(Pet.class))).thenReturn(pet1, pet2);
        when(petRepo.getPetsByColor(AvailableColor.Red)).thenReturn(List.of(pet1, pet2));

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

        Person owner = new Person();
        owner.setId(1L);
        owner.setName("Owner1");
        owner.setBirthdate(Date.valueOf("2020-01-01"));

        when(personRepo.save(any(Person.class))).thenReturn(owner);

        Long ownerId = personServ.createPerson(ownerDto);

        PetDto petDto = PetDto.builder()
                .name("Pet11")
                .breed("Breed11")
                .color(AvailableColor.Red)
                .ownerId(ownerId)
                .build();

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Pet11");
        pet.setBreed("Breed11");
        pet.setColor(AvailableColor.Red);
        pet.setOwner(owner);

        when(petRepo.save(any(Pet.class))).thenReturn(pet);
        when(petRepo.getById(1L)).thenReturn(pet);

        Long petId = petServ.createPet(petDto);

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

        Pet friend1 = new Pet();
        friend1.setId(1L);
        friend1.setName("Friend1");
        friend1.setBreed("Breed12");
        friend1.setColor(AvailableColor.Blue);

        Pet friend2 = new Pet();
        friend2.setId(2L);
        friend2.setName("Friend2");
        friend2.setBreed("Breed13");
        friend2.setColor(AvailableColor.Green);

        when(petRepo.save(any(Pet.class))).thenReturn(friend1, friend2);
        when(petRepo.getPetsByIds(Set.of(1L, 2L))).thenReturn(List.of(friend1, friend2));

        Long friendId1 = petServ.createPet(friendDto1);
        Long friendId2 = petServ.createPet(friendDto2);

        PetDto petDto = PetDto.builder()
                .name("Pet12")
                .breed("Breed14")
                .color(AvailableColor.Red)
                .friendsIds(Set.of(friendId1, friendId2))
                .build();

        Pet pet = new Pet();
        pet.setId(3L);
        pet.setName("Pet12");
        pet.setBreed("Breed14");
        pet.setColor(AvailableColor.Red);
        pet.setFriends(Set.of(friend1, friend2));

        when(petRepo.save(any(Pet.class))).thenReturn(pet);
        when(petRepo.getById(3L)).thenReturn(pet);

        Long petId = petServ.createPet(petDto);

        PetDto createdPet = petServ.getPetById(petId);
        assertEquals(2, createdPet.getFriendsIds().size());
    }

    @Test
    void testUpdatePetWithNewOwner() {
        PersonDto ownerDto = PersonDto.builder()
                .name("Owner2")
                .build();

        Person owner = new Person();
        owner.setId(1L);
        owner.setName("Owner2");

        when(personRepo.save(any(Person.class))).thenReturn(owner);

        Long ownerId = personServ.createPerson(ownerDto);

        PetDto petDto = PetDto.builder()
                .name("Pet13")
                .breed("Breed15")
                .color(AvailableColor.Red)
                .build();

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Pet13");
        pet.setBreed("Breed15");
        pet.setColor(AvailableColor.Red);

        when(petRepo.save(any(Pet.class))).thenReturn(pet);
        when(petRepo.getById(1L)).thenReturn(pet);

        Long petId = petServ.createPet(petDto);

        PetDto updatedPetDto = PetDto.builder()
                .id(petId)
                .name("Pet13")
                .breed("Breed15")
                .color(AvailableColor.Red)
                .ownerId(ownerId)
                .build();

        petServ.updatePet(updatedPetDto);

        owner.setId(ownerId);
        pet.setOwner(owner);

        PetDto updatedPet = petServ.getPetById(petId);
        assertEquals(ownerId, updatedPet.getOwnerId());
    }
}
