package ru.pressstart9.petproject.person_ms.service;

import ru.pressstart9.petproject.commons.dto.requests.GetByOwner;
import ru.pressstart9.petproject.commons.dto.requests.RemovePetOwner;
import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.person_ms.dao.PersonRepository;
import ru.pressstart9.petproject.person_ms.domain.Person;
import org.springframework.stereotype.Service;
import ru.pressstart9.petproject.commons.dto.responses.PersonDto;
import ru.pressstart9.petproject.person_ms.service.kafka.RequestProducer;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class PersonService {
    private final RequestProducer requestProducer;
    private final PersonRepository personRepository;

    public PersonService(RequestProducer requestProducer, PersonRepository personRepository) {
        this.requestProducer = requestProducer;
        this.personRepository = personRepository;
    }

    public Long createPerson(String name, Date birthdate) {
        return personRepository.save(
                new Person(name, birthdate)).getId();
    }

    public PersonDto getPersonDtoById(long id) {
        return convertToDto(getPersonById(id));
    }

    public List<PersonDto> getAllPeople() {
        List<Person> people = personRepository.findAll();
        return people.stream().map(this::convertToDto).toList();
    }

    public void deletePersonById(long id) {
        requestProducer.sendPetRequest(new RemovePetOwner(id));
        personRepository.deleteById(id);
    }

    private Person getPersonById(long id) {
        return personRepository.findById(id).orElseThrow(() -> new EntityNotFound(String.valueOf(id)));
    }

    public PersonDto convertToDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .birthdate(person.getBirthdate())
                .petsIds(new HashSet<>(requestProducer.sendPetRequest(new GetByOwner(person.getId()))))
                .build();
    }
}
