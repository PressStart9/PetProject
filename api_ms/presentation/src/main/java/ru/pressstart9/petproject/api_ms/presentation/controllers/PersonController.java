package ru.pressstart9.petproject.api_ms.presentation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.pressstart9.petproject.api_ms.service.UserInfoService;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.commons.dto.requests.DeleteRequest;
import ru.pressstart9.petproject.commons.dto.responses.CreatedResponse;
import ru.pressstart9.petproject.commons.dto.requests.GetRequest;
import ru.pressstart9.petproject.commons.dto.responses.PersonDto;
import ru.pressstart9.petproject.commons.dto.requests.CreatePersonBody;

@RestController
@RequestMapping("/people")
public class PersonController {
    private final UserInfoService userInfoService;

    private final RequestProducer requestProducer;

    public PersonController(UserInfoService userInfoService, RequestProducer requestProducer) {
        this.userInfoService = userInfoService;
        this.requestProducer = requestProducer;
    }

    @PostMapping
    @PreAuthorize("@permission.isAdmin()")
    public ResponseEntity<Long> createPerson(@Valid @RequestBody CreatePersonBody request) {
        CreatedResponse response = requestProducer.sendPersonRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable("id") Long id) {
        PersonDto response = requestProducer.sendPersonRequest(new GetRequest(id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permission.isSelf(#id)")
    public ResponseEntity<Void> deletePerson(@P("id") @PathVariable("id") Long id) {
        requestProducer.sendPersonRequest(new DeleteRequest(id));
        userInfoService.deleteUserInfo(id);
        return ResponseEntity.noContent().build();
    }
}
