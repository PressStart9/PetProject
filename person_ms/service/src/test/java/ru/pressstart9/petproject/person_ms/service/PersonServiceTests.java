package ru.pressstart9.petproject.person_ms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.person_ms.dao.PersonRepository;
import ru.pressstart9.petproject.person_ms.domain.Person;
import ru.pressstart9.petproject.commons.dto.responses.PersonDto;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonServiceTests {
    @Mock
    private PersonRepository personRepo;

    @InjectMocks
    private PersonService personServ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePerson() {
        Person person = new Person("Ivan",
                Date.valueOf("2025-01-01"));
        person.setId(1L);

        PersonDto personDto = PersonService.convertToDto(person);

        when(personRepo.save(any(Person.class))).thenReturn(person);
        when(personRepo.findById(1L)).thenReturn(Optional.of(person));

        Long id = personServ.createPerson(personDto.getName(), personDto.getBirthdate());

        PersonDto createdPerson = personServ.getPersonDtoById(id);
        assertNotNull(createdPerson);
        assertEquals(person.getName(), createdPerson.getName());
    }

    @Test
    void testGetPersonDtoById() {
        Person person = new Person("Ivan",
                Date.valueOf("2025-01-01"));
        person.setId(1L);

        when(personRepo.findById(1L)).thenReturn(Optional.of(person));

        PersonDto personDto = personServ.getPersonDtoById(1L);
        assertNotNull(personDto);
        assertEquals(person.getName(), personDto.getName());
    }

    @Test
    void testGetAllPeople() {
        Person person1 = new Person("Ivan",
                Date.valueOf("2025-01-01"));
        person1.setId(1L);

        Person person2 = new Person("Anna",
                Date.valueOf("2025-02-01"));
        person2.setId(2L);

        when(personRepo.findAll()).thenReturn(List.of(person1, person2));

        List<PersonDto> allPersons = personServ.getAllPeople();
        assertEquals(2, allPersons.size());
    }

    @Test
    void testDeletePersonById() {
        Person person = new Person("Ivan",
                Date.valueOf("2025-01-01"));
        person.setId(1L);

        when(personRepo.findById(1L)).thenReturn(Optional.of(person));

        personServ.deletePersonById(1L);
        verify(personRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePersonByIdNotFound() {
        when(personRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFound.class, () -> personServ.deletePersonById(1L));
    }
}
