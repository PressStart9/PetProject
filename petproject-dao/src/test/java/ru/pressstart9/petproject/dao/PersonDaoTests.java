package ru.pressstart9.petproject.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.domain.Person;
import ru.pressstart9.petproject.domain.Pet;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonDaoTests extends BaseTestContainer {
    @BeforeEach
    void setUp() {
        petRepo.deleteAll();
        personRepo.deleteAll();
    }

    @Autowired
    PetRepository petRepo;
    @Autowired
    PersonRepository personRepo;

    @Test
    public void testSaveAndGetPerson() {
        Person person = new Person("Barsik", Date.valueOf("2025-01-01"));

        personRepo.save(person);

        Person persievedPerson = personRepo.findById(person.getId()).orElseThrow();

        assertEquals(person, persievedPerson);
    }

    @Test
    public void testUpdatePerson() {
        Person person = new Person("Barsik", Date.valueOf("2025-01-01"));

        personRepo.save(person);

        var newName = "Updated Barsik";
        person.setName(newName);
        personRepo.save(person);

        Person updatedPerson = personRepo.findById(person.getId()).orElseThrow();

        assertEquals(newName, updatedPerson.getName());
    }

    @Test
    public void testDeletePerson() {
        Person person = new Person("Barsik", Date.valueOf("2025-01-01"));

        personRepo.save(person);

        personRepo.deleteById(person.getId());

        assertTrue(personRepo.findById(person.getId()).isEmpty());
    }

    @Test
    public void testGetAllPeople() {
        Person person1 = new Person("Person1", Date.valueOf("2025-01-01"));

        Person person2 = new Person("Person2", Date.valueOf("2025-01-01"));

        personRepo.save(person1);
        personRepo.save(person2);

        List<Person> people = personRepo.findAll();

        assertIterableEquals(List.of(person1, person2), people);
    }

    @Test
    public void testAddPet() {
        Person person = new Person("Person1", Date.valueOf("2025-01-01"));

        Pet pet = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);

        person.addPet(pet);

        assertIterableEquals(List.of(pet), person.getPets());
        assertEquals(person, pet.getOwner());
    }

    @Test
    public void testAddSomeonesPet() {
        Person person1 = new Person("Person1", Date.valueOf("2025-01-01"));

        Person person2 = new Person("Person2", Date.valueOf("2025-01-01"));

        Pet pet = new Pet("Pet1",
                Date.valueOf("2025-01-01"),
                "Siamese",
                AvailableColor.black);

        assertTrue(person1.addPet(pet));
        assertFalse(person2.addPet(pet));
    }
}
