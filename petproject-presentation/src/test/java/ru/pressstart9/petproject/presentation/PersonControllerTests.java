package ru.pressstart9.petproject.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.dto.responses.PersonDto;
import ru.pressstart9.petproject.dto.auth.CreateAccountBody;
import ru.pressstart9.petproject.presentation.controllers.PersonController;
import ru.pressstart9.petproject.service.AuthService;
import ru.pressstart9.petproject.service.PersonService;
import ru.pressstart9.petproject.service.PetService;
import ru.pressstart9.petproject.service.UserInfoService;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = { "admin" })
@WebMvcTest(PersonController.class)
public class PersonControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;
    @MockitoBean
    private PetService petService;
    @MockitoBean
    private UserInfoService userInfoService;
    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePerson() throws Exception {
        CreateAccountBody createAccountBody = new CreateAccountBody();
        createAccountBody.setName("Ivan");
        createAccountBody.setBirthdate(Date.valueOf("2025-01-01"));
        when(personService.createPerson(any(), any())).thenReturn(1L);

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void testCreateBlankPerson() throws Exception {
        CreateAccountBody createAccountBody = new CreateAccountBody();
        createAccountBody.setName("      ");
        createAccountBody.setBirthdate(Date.valueOf("2025-01-01"));
        when(personService.createPerson(any(), any())).thenReturn(1L);

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFuturePerson() throws Exception {
        CreateAccountBody createAccountBody = new CreateAccountBody();
        createAccountBody.setName("Ivan");
        // Rewrite it after 9999 year
        createAccountBody.setBirthdate(Date.valueOf("9999-01-01"));
        when(personService.createPerson(any(), any())).thenReturn(1L);

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPerson() throws Exception {
        PersonDto personDto = PersonDto.builder()
                .id(1L)
                .name("Ivan")
                .birthdate(Date.valueOf("2025-01-01"))
                .build();
        when(personService.getPersonDtoById(anyLong())).thenReturn(personDto);

        mockMvc.perform(get("/people/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    void testGetNonExistingPerson() throws Exception {
        when(personService.getPersonDtoById(anyLong())).thenThrow(new EntityNotFound(1L));

        mockMvc.perform(get("/people/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/people/1"))
                .andExpect(status().isNoContent());
    }
}
