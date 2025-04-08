package org.example.services;

import lombok.AllArgsConstructor;
import org.example.DaoFactory;
import org.example.dto.PersonDto;
import org.example.entities.Person;
import org.example.entities.Pet;
import org.example.repositories.PersonRepository;

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

    private PersonDto convertToDto(Person person) {
        return daoFactory.inTransaction(entityManager -> {
            entityManager.refresh(person);
            return PersonDto.builder()
                    .id(person.getId())
                    .name(person.getName())
                    .birthdate(person.getBirthdate())
                    .petsIds(person.getPets().stream().map(Pet::getId).collect(Collectors.toSet()))
                    .build();
        });
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
