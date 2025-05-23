package ru.pressstart9.petproject.person_ms.service;

import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.person_ms.dao.PersonRepository;
import ru.pressstart9.petproject.person_ms.domain.Person;
import org.springframework.stereotype.Service;
import ru.pressstart9.petproject.commons.dto.responses.PersonDto;

import java.util.Date;
import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Long createPerson(String name, Date birthdate) {
        return personRepository.save(
                new Person(name, birthdate)).getId();
    }

    public PersonDto getPersonDtoById(long id) {
        return convertToDto(getPersonById(id));
    }

    public void removePetById(long ownerId, long petId) {
        getPersonById(ownerId).removePet(petId);
    }

    public List<PersonDto> getAllPeople() {
        List<Person> people = personRepository.findAll();
        return people.stream().map(PersonService::convertToDto).toList();
    }

    public void deletePersonById(long id) {
        Person deletePerson = getPersonById(id);
        deletePerson.getPetIds().forEach(deletePerson::removePet);
        personRepository.deleteById(id);
    }

    private Person getPersonById(long id) {
        return personRepository.findById(id).orElseThrow(() -> new EntityNotFound(String.valueOf(id)));
    }

    public static PersonDto convertToDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .birthdate(person.getBirthdate())
                .petsIds(person.getPetIds())
                .build();
    }
}
