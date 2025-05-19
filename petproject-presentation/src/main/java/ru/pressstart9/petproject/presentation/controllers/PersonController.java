package ru.pressstart9.petproject.presentation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.pressstart9.petproject.dto.responses.PersonDto;
import ru.pressstart9.petproject.dto.requests.CreatePersonBody;
import ru.pressstart9.petproject.service.PersonService;

@RestController
@RequestMapping("/people")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @PreAuthorize("@permission.isAdmin()")
    public ResponseEntity<Long> createPerson(@Valid @RequestBody CreatePersonBody request) {
        Long id = personService.createPerson(request.getName(), request.getBirthdate());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personService.getPersonDtoById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.isSelf(#id)")
    public ResponseEntity<Void> deletePerson(@P("id") @PathVariable("id") Long id) {
        personService.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }
}
