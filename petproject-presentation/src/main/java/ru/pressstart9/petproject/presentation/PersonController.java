package ru.pressstart9.petproject.presentation;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.pressstart9.petproject.dto.PersonDto;
import ru.pressstart9.petproject.presentation.bodies.CreatePersonBody;
import ru.pressstart9.petproject.service.PersonService;

@RequestMapping("/people")
@RestController
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public Long createPerson(@RequestBody CreatePersonBody createPersonRequest, BindingResult result) {
        return personService.createPerson(createPersonRequest.getName(), createPersonRequest.getBirthdate());
    }

    @GetMapping("/{id}")
    public PersonDto getPerson(@PathVariable("id") Long personId) {
        return personService.getPersonDtoById(personId);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable("id") Long personId) {
        personService.deletePersonById(personId);
    }
}
