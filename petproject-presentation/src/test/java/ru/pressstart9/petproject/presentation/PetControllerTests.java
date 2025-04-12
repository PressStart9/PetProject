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
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.dto.PetDto;
import ru.pressstart9.petproject.presentation.bodies.CreatePetBody;
import ru.pressstart9.petproject.service.PetService;

import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
public class PetControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

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
        when(petService.createPet(any(), any(), any(), any())).thenReturn(1L);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void testReadPet() throws Exception {
        PetDto petDto = PetDto.builder()
                .id(1L)
                .name("Barsik")
                .birthdate(Date.valueOf("2025-01-01"))
                .breed("Siamese")
                .color(AvailableColor.black)
                .build();
        when(petService.getPetDtoById(anyLong())).thenReturn(petDto);

        mockMvc.perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Barsik"));
    }

    @Test
    void testDeletePet() throws Exception {
        mockMvc.perform(delete("/pets/1"))
                .andExpect(status().isOk());
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
        when(petService.getByParams(any(), any(), any(), anyInt(), anyInt())).thenReturn(List.of(petDto1, petDto2));

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
    void testAddPetFriend() throws Exception {
        mockMvc.perform(post("/pets/friends/1/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testRemovePetFriend() throws Exception {
        mockMvc.perform(delete("/pets/friends/1/2"))
                .andExpect(status().isOk());
    }
}
