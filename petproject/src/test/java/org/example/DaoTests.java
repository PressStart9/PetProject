package org.example;

import lombok.val;
import org.example.entities.AvailableColor;
import org.example.entities.Person;
import org.example.entities.Pet;
import org.example.repositories.PersonRepository;
import org.example.repositories.PetRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DaoTests {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    static DaoFactory daoFactory;

    static PetRepository petRepo;
    static PersonRepository personRepo;

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
    public void testSaveAndGetPet() {
        Pet pet = new Pet();
        pet.setName("Barsik");
        pet.setBreed("Siamese");
        pet.setColor(AvailableColor.Black);

        petRepo.save(pet);

        Pet retrievedPet = petRepo.getById(pet.getId());

        assertEquals(pet, retrievedPet);
    }

    @Test
    public void testUpdatePet() {
        Pet pet = new Pet();
        pet.setName("Murzik");
        pet.setBreed("Persian");
        pet.setColor(AvailableColor.Black);

        petRepo.save(pet);

        val newName = "Updated Murzik";
        pet.setName(newName);
        petRepo.update(pet);

        Pet updatedPet = petRepo.getById(pet.getId());

        assertEquals(newName, updatedPet.getName());
    }

    @Test
    public void testDeletePet() {
        Pet pet = new Pet();
        pet.setName("Vasya");
        pet.setBreed("Sphynx");
        pet.setColor(AvailableColor.Black);

        petRepo.save(pet);

        petRepo.deleteById(pet.getId());

        Pet deletedPet = petRepo.getById(pet.getId());

        assertNull(deletedPet);
    }

    @Test
    public void testGetAllPets() {
        Pet pet1 = new Pet();
        pet1.setName("Pet1");
        pet1.setBreed("Maine Coon");
        pet1.setColor(AvailableColor.Green);

        Pet pet2 = new Pet();
        pet2.setName("Pet2");
        pet2.setBreed("Ragdoll");
        pet2.setColor(AvailableColor.Blue);

        petRepo.save(pet1);
        petRepo.save(pet2);

        List<Pet> pets = petRepo.getAll();

        assertIterableEquals(List.of(pet1, pet2), pets);
    }

    @Test
    public void testAddFriends() {
        Pet pet1 = new Pet();
        pet1.setName("Pet1");
        pet1.setBreed("Maine Coon");
        pet1.setColor(AvailableColor.Green);
        petRepo.save(pet1);

        Pet pet2 = new Pet();
        pet2.setName("Pet2");
        pet2.setBreed("Ragdoll");
        pet2.setColor(AvailableColor.Blue);
        petRepo.save(pet2);

        pet1.getFriends().add(pet2);

        List<Pet> pets = petRepo.getAll();

        assertIterableEquals(List.of(pet1, pet2), pets);
    }

    @Test
    public void testAddFriendWithoutPersist() {
        Pet pet1 = new Pet();
        pet1.setName("Pet1");
        pet1.setBreed("Maine Coon");
        pet1.setColor(AvailableColor.Green);
        petRepo.save(pet1);

        Pet pet2 = new Pet();
        pet2.setName("Pet2");
        pet2.setBreed("Ragdoll");
        pet2.setColor(AvailableColor.Blue);

        pet1.getFriends().add(pet2);

        List<Pet> pets = petRepo.getAll();

        assertIterableEquals(List.of(pet1), pets);
        assertIterableEquals(List.of(pet2), pet1.getFriends());
    }

    @Test
    public void testSaveAndGetPerson() {
        Person person = new Person();
        person.setName("Barsik");
        person.setBirthdate(Date.valueOf("2025-01-01"));

        personRepo.save(person);

        Person retrievedPerson = personRepo.getById(person.getId());

        assertEquals(person, retrievedPerson);
    }

    @Test
    public void testUpdatePerson() {
        Person person = new Person();
        person.setName("Barsik");
        person.setBirthdate(Date.valueOf("2025-01-01"));

        personRepo.save(person);

        val newName = "Updated Murzik";
        person.setName(newName);
        personRepo.update(person);

        Person updatedPerson = personRepo.getById(person.getId());

        assertEquals(newName, updatedPerson.getName());
    }

    @Test
    public void testDeletePerson() {
        Person person = new Person();
        person.setName("Barsik");
        person.setBirthdate(Date.valueOf("2025-01-01"));

        personRepo.save(person);

        personRepo.deleteById(person.getId());

        Person deletedPerson = personRepo.getById(person.getId());

        assertNull(deletedPerson);
    }

    @Test
    public void testGetAllPeople() {
        Person person1 = new Person();
        person1.setName("Barsik");
        person1.setBirthdate(Date.valueOf("2025-01-01"));

        Person person2 = new Person();
        person2.setName("Murzik");
        person2.setBirthdate(Date.valueOf("2020-01-01"));

        personRepo.save(person1);
        personRepo.save(person2);

        List<Person> people = personRepo.getAll();

        assertIterableEquals(List.of(person1, person2), people);
    }

    @Test
    public void testAddPets() {
        Person person = new Person();
        person.setName("Barsik");
        person.setBirthdate(Date.valueOf("2025-01-01"));
        personRepo.save(person);

        Pet pet = new Pet();
        pet.setName("Pet1");
        pet.setBreed("Maine Coon");
        pet.setColor(AvailableColor.Green);
        petRepo.save(pet);

        person.getPets().add(pet);

        List<Pet> pets = petRepo.getAll();

        assertIterableEquals(List.of(pet), pets);
        assertNull(pet.getOwner());
    }

    @Test
    public void testAddPetsWithoutPersist() {
        Person person = new Person();
        person.setName("Barsik");
        person.setBirthdate(Date.valueOf("2025-01-01"));
        personRepo.save(person);

        Pet pet = new Pet();
        pet.setName("Pet1");
        pet.setBreed("Maine Coon");
        pet.setColor(AvailableColor.Green);

        person.getPets().add(pet);

        List<Pet> pets = petRepo.getAll();

        assertIterableEquals(List.of(), pets);
        assertNull(pet.getOwner());
    }
}
