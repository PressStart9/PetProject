package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dao.DaoFactory;
import org.example.dto.PersonDto;
import org.example.entities.Person;
import org.example.entities.Pet;
import org.example.dao.PersonRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PersonService {
    private final DaoFactory daoFactory;
    private final PersonRepository personRepository;

    public Long createPerson(PersonDto personDto) {
        return personRepository.save(convertToEntity(personDto)).getId();
    }

    public PersonDto getPersonById(long id) {
        Person person = personRepository.getById(id);
        return person != null ? convertToDto(person) : null;
    }

    public List<PersonDto> getAllPeople() {
        List<Person> pets = personRepository.getAll();
        return pets.stream().map(this::convertToDto).toList();
    }

    public void updatePerson(PersonDto personDto) {
        personRepository.update(convertToEntity(personDto));
    }

    public void deletePersonById(long id) {
        personRepository.deleteById(id);
    }

    public void deleteAllPeople() {
        personRepository.deleteAll();
    }

    public PersonDto convertToDto(Person person) {
        var updatedPerson = personRepository.getById(person.getId());
        return PersonDto.builder()
                .id(updatedPerson.getId())
                .name(updatedPerson.getName())
                .birthdate(updatedPerson.getBirthdate())
                .petsIds(updatedPerson.getPets().stream().map(Pet::getId).collect(Collectors.toSet()))
                .build();
    }

    private Person convertToEntity(PersonDto personDto) {
        Person person = new Person();
        person.setId(personDto.getId());
        person.setName(personDto.getName());
        person.setBirthdate(personDto.getBirthdate());

        person.setPets(personDto.getPetsIds() == null ? null :
                new HashSet<>(daoFactory.getPetRepository().getPetsByIds(personDto.getPetsIds())));

        return person;
    }
}
