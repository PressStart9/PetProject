package ru.pressstart9.petproject.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pressstart9.petproject.dto.PersonDto;
import ru.pressstart9.petproject.presentation.bodies.CreatePersonBody;
import ru.pressstart9.petproject.service.PersonService;

@RestController
@RequestMapping("/people")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Long> createPerson(@RequestBody CreatePersonBody request) {
        Long id = personService.createPerson(request.getName(), request.getBirthdate());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personService.getPersonDtoById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") Long id) {
        personService.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }
}

