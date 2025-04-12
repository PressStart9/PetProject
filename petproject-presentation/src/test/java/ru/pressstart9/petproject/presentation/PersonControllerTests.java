package ru.pressstart9.petproject.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pressstart9.petproject.dto.PersonDto;
import ru.pressstart9.petproject.presentation.bodies.CreatePersonBody;
import ru.pressstart9.petproject.service.PersonService;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePerson() throws Exception {
        CreatePersonBody createPersonBody = new CreatePersonBody();
        createPersonBody.setName("Ivan");
        createPersonBody.setBirthdate(Date.valueOf("2025-01-01"));
        when(personService.createPerson(any(), any())).thenReturn(1L);

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPersonBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
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
    void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/people/1"))
                .andExpect(status().isOk());
    }
}
