package ru.pressstart9.petproject.person_ms.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.pressstart9.petproject.person_ms.domain.Person;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonDaoTests extends BaseTestContainer {
    @BeforeEach
    void setUp() {
        personRepo.deleteAll();
    }

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

        person.addPet(1L);

        assertIterableEquals(List.of(1L), person.getPetIds());
    }
}
