package ru.pressstart9.petproject.api_ms.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pressstart9.petproject.api_ms.service.AuthService;
import ru.pressstart9.petproject.api_ms.service.UserInfoService;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.commons.dto.requests.GetRequest;
import ru.pressstart9.petproject.commons.dto.responses.CreatedResponse;
import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.commons.dto.requests.CreatePersonBody;
import ru.pressstart9.petproject.commons.dto.responses.PersonDto;
import ru.pressstart9.petproject.api_ms.presentation.controllers.PersonController;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser(authorities = { "admin" })
@WebMvcTest(PersonController.class)
@ContextConfiguration(classes = {TestApplication.class})
public class PersonControllerAdminTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserInfoService userInfoService;
    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private RequestProducer requestProducer;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public static CreatePersonBody getValidCreatePersonBody() {
        return new CreatePersonBody("Ivan", Date.valueOf("2025-01-01"));
    }

    @Test
    void testCreatePerson() throws Exception {
        when(requestProducer.sendPersonRequest(any(CreatePersonBody.class))).thenReturn(new CreatedResponse(1L));

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getValidCreatePersonBody())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    void testCreateBlankPerson() throws Exception {
        CreatePersonBody CreatePersonBody = getValidCreatePersonBody();
        CreatePersonBody.setName("   ");

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CreatePersonBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFuturePerson() throws Exception {
        CreatePersonBody CreatePersonBody = getValidCreatePersonBody();
        // Rewrite it after 9999 year
        CreatePersonBody.setBirthdate(Date.valueOf("9999-01-01"));

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CreatePersonBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPerson() throws Exception {
        PersonDto personDto = PersonDto.builder()
                .id(1L)
                .name("Ivan")
                .birthdate(Date.valueOf("2025-01-01"))
                .build();
        when(requestProducer.sendPersonRequest(any(GetRequest.class))).thenReturn(personDto);

        mockMvc.perform(get("/people/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    void testGetNonExistingPerson() throws Exception {
        when(requestProducer.sendPersonRequest(any(GetRequest.class))).thenThrow(new EntityNotFound(String.valueOf(1L)));

        mockMvc.perform(get("/people/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/people/1"))
                .andExpect(status().isNoContent());
    }
}
