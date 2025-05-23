package ru.pressstart9.petproject.api_ms.presentation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.dto.CreatedResponse;
import ru.pressstart9.petproject.dto.requests.GetRequest;
import ru.pressstart9.petproject.dto.PersonDto;
import ru.pressstart9.petproject.dto.requests.CreatePersonBody;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/people")
public class PersonController {
    private final RequestProducer requestProducer;

    public PersonController(RequestProducer requestProducer) {
        this.requestProducer = requestProducer;
    }

    @PostMapping
    @PreAuthorize("@permission.isAdmin()")
    public ResponseEntity<Long> createPerson(@Valid @RequestBody CreatePersonBody request) throws ExecutionException, InterruptedException {
        CreatedResponse response = requestProducer.sendPersonRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable("id") Long id) {
        PersonDto response = requestProducer.sendPersonRequest(new GetRequest(id));
        return ResponseEntity.ok(response);
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("@permission.isSelf(#id)")
//    public ResponseEntity<Void> deletePerson(@P("id") @PathVariable("id") Long id) {
//        personService.deletePersonById(id);
//        return ResponseEntity.noContent().build();
//    }
}
