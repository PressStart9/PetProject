package ru.pressstart9.petproject.api_ms.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pressstart9.petproject.api_ms.service.AuthService;
import ru.pressstart9.petproject.api_ms.service.UserInfoService;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.commons.dto.requests.GetRequest;
import ru.pressstart9.petproject.commons.dto.requests.PetFilterRequest;
import ru.pressstart9.petproject.commons.dto.responses.CreatedResponse;
import ru.pressstart9.petproject.commons.exceptions.EntityNotFound;
import ru.pressstart9.petproject.commons.dto.responses.PetDto;
import ru.pressstart9.petproject.commons.dto.requests.CreatePetBody;
import ru.pressstart9.petproject.commons.dto.requests.FriendPairBody;
import ru.pressstart9.petproject.api_ms.presentation.controllers.PetController;

import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser(authorities = { "admin" })
@WebMvcTest(PetController.class)
@ContextConfiguration(classes = {TestApplication.class})
public class PetControllerAdminTests {
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

    @Test
    void testCreatePet() throws Exception {
        CreatePetBody createPetBody = new CreatePetBody();
        createPetBody.setName("Barsik");
        createPetBody.setBirthdate(Date.valueOf("2025-01-01"));
        createPetBody.setBreed("Siamese");
        createPetBody.setColor("black");

        when(requestProducer.sendPetRequest(any(CreatePetBody.class))).thenReturn(new CreatedResponse(1L));
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void testCreateSamePet() throws Exception {
        CreatePetBody createPetBody = new CreatePetBody();
        createPetBody.setName("Barsik");
        createPetBody.setBirthdate(Date.valueOf("2025-01-01"));
        createPetBody.setBreed("Siamese");
        createPetBody.setColor("black");

        when(requestProducer.sendPetRequest(any(CreatePetBody.class))).thenReturn(new CreatedResponse(1L));
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1));

        when(requestProducer.sendPetRequest(any(CreatePetBody.class))).thenReturn(new CreatedResponse(2L));
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(2));
    }

    @Test
    void testCreateBlankPet() throws Exception {
        CreatePetBody createPetBody = new CreatePetBody();
        createPetBody.setName("      ");
        createPetBody.setBirthdate(Date.valueOf("2025-01-01"));
        createPetBody.setBreed("Siamese");
        createPetBody.setColor("black");

        when(requestProducer.sendPetRequest(any(CreatePetBody.class))).thenReturn(new CreatedResponse(1L));
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFuturePet() throws Exception {
        CreatePetBody createPetBody = new CreatePetBody();
        createPetBody.setName("Murzik");
        // // Rewrite it after 9999 year
        createPetBody.setBirthdate(Date.valueOf("9999-01-01"));
        createPetBody.setBreed("Siamese");
        createPetBody.setColor("black");

        when(requestProducer.sendPetRequest(any(CreatePetBody.class))).thenReturn(new CreatedResponse(1L));
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReadPet() throws Exception {
        PetDto petDto = PetDto.builder()
                .id(1L)
                .name("Barsik")
                .birthdate(Date.valueOf("2025-01-01"))
                .build();
        when(requestProducer.sendPetRequest(any(GetRequest.class))).thenReturn(petDto);

        mockMvc.perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testReadNonExistingPet() throws Exception {
        when(requestProducer.sendPetRequest(any(GetRequest.class))).thenThrow(new EntityNotFound(String.valueOf(1L)));

        mockMvc.perform(get("/pets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePet() throws Exception {
        mockMvc.perform(delete("/pets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetFilteredPets() throws Exception {
        PetDto petDto1 = PetDto.builder()
                .id(1L)
                .name("Barsik")
                .birthdate(Date.valueOf("2025-01-01"))
                .breed("Siamese")
                .color(AvailableColor.black)
                .build();
        PetDto petDto2 = PetDto.builder()
                .id(2L)
                .name("Murka")
                .birthdate(Date.valueOf("2025-02-01"))
                .breed("Persian")
                .color(AvailableColor.white)
                .build();
        when(requestProducer.sendPetRequest(any(PetFilterRequest.class))).thenReturn(List.of(petDto1, petDto2));

        mockMvc.perform(get("/pets")
                        .param("name", "")
                        .param("breed", "")
                        .param("colors", "")
                        .param("size", "5")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetFilteredPetsWithWrongArguments() throws Exception {
        mockMvc.perform(get("/pets")
                        .param("name", "")
                        .param("breed", "")
                        .param("colors", "")
                        .param("size", "0")
                        .param("page", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/pets")
                        .param("name", "")
                        .param("breed", "")
                        .param("colors", "")
                        .param("size", "-1")
                        .param("page", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/pets")
                        .param("name", "")
                        .param("breed", "")
                        .param("colors", "")
                        .param("size", "1")
                        .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddPetFriend() throws Exception {
        FriendPairBody friendPairBody = new FriendPairBody(1L, 2L);
        mockMvc.perform(put("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendPairBody)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddSamePetFriend() throws Exception {
        FriendPairBody friendPairBody = new FriendPairBody(1L, 2L);
        mockMvc.perform(put("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendPairBody)))
                .andExpect(status().isNoContent());
        mockMvc.perform(put("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendPairBody)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemovePetFriend() throws Exception {
        FriendPairBody friendPairBody = new FriendPairBody(1L, 2L);
        mockMvc.perform(delete("/pets/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(friendPairBody)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemoveSamePetFriend() throws Exception {
        FriendPairBody friendPairBody = new FriendPairBody(1L, 2L);
        mockMvc.perform(delete("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendPairBody)))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendPairBody)))
                .andExpect(status().isNoContent());
    }
}
