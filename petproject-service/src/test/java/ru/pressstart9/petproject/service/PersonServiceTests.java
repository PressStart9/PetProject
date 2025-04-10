package ru.pressstart9.petproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.pressstart9.petproject.dao.PersonRepository;
import ru.pressstart9.petproject.dao.PetRepository;
import ru.pressstart9.petproject.domain.Person;
import ru.pressstart9.petproject.dto.PersonDto;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonServiceTests {
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
    void testCreatePerson() {
        Person person = new Person("Barsik",
                Date.valueOf("2025-01-01"));
        person.setId(1L);

        PersonDto personDto = PersonService.convertToDto(person);

        when(personRepo.save(any(Person.class))).thenReturn(person);
        when(personRepo.findById(1L)).thenReturn(Optional.of(person));

        Long id = personServ.createPerson(personDto);

        PersonDto createdPerson = personServ.getPersonDtoById(id);
        assertNotNull(createdPerson);
        assertEquals(person.getName(), createdPerson.getName());
    }

    @Test
    void testGetAllPets() {
        Person person1 = new Person("Person1",
                Date.valueOf("2025-01-01"));
        person1.setId(1L);

        Person person2 = new Person("Pet2",
                Date.valueOf("2025-01-01"));
        person1.setId(2L);

        when(personRepo.findAll()).thenReturn(List.of(person1, person2));

        List<PersonDto> allPersons = personServ.getAllPeople();
        assertEquals(2, allPersons.size());
    }
}
