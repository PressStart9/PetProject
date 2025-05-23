//package ru.pressstart9.petproject.api_ms.presentation
//
//-microservice.presentation;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.pressstart9.petproject.common_kafka.AvailableColor;
//import ru.pressstart9.petproject.common_kafka.exceptions.EntityNotFound;
//import ru.pressstart9.petproject.dto.responses.PetDto;
//import ru.pressstart9.petproject.dto.requests.CreatePetBody;
//import ru.pressstart9.petproject.dto.requests.FriendPairBody;
//import ru.pressstart9.petproject.presentation.controllers.PetController;
//import ru.pressstart9.petproject.service.AuthService;
//import ru.pressstart9.petproject.service.PersonService;
//import ru.pressstart9.petproject.service.PetService;
//import ru.pressstart9.petproject.service.UserInfoService;
//
//import java.sql.Date;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WithMockUser(authorities = { "admin" })
//@WebMvcTest(PetController.class)
//public class PetControllerAdminTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private PersonService personService;
//    @MockitoBean
//    private PetService petService;
//    @MockitoBean
//    private UserInfoService userInfoService;
//    @MockitoBean
//    private AuthService authService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreatePet() throws Exception {
//        CreatePetBody createPetBody = new CreatePetBody();
//        createPetBody.setName("Barsik");
//        createPetBody.setBirthdate(Date.valueOf("2025-01-01"));
//        createPetBody.setBreed("Siamese");
//        createPetBody.setColor("black");
//
//        when(petService.createPet(any(), any(), any(), any())).thenReturn(1L);
//        mockMvc.perform(post("/pets")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createPetBody)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$").value(1));
//    }
//
//    @Test
//    void testCreateSamePet() throws Exception {
//        CreatePetBody createPetBody = new CreatePetBody();
//        createPetBody.setName("Barsik");
//        createPetBody.setBirthdate(Date.valueOf("2025-01-01"));
//        createPetBody.setBreed("Siamese");
//        createPetBody.setColor("black");
//
//        when(petService.createPet(any(), any(), any(), any())).thenReturn(1L);
//        mockMvc.perform(post("/pets")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createPetBody)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$").value(1));
//
//        when(petService.createPet(any(), any(), any(), any())).thenReturn(2L);
//        mockMvc.perform(post("/pets")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createPetBody)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$").value(2));
//    }
//
//    @Test
//    void testCreateBlankPet() throws Exception {
//        CreatePetBody createPetBody = new CreatePetBody();
//        createPetBody.setName("      ");
//        createPetBody.setBirthdate(Date.valueOf("2025-01-01"));
//        createPetBody.setBreed("Siamese");
//        createPetBody.setColor("black");
//
//        when(petService.createPet(any(), any(), any(), any())).thenReturn(1L);
//        mockMvc.perform(post("/pets")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createPetBody)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testCreateFuturePet() throws Exception {
//        CreatePetBody createPetBody = new CreatePetBody();
//        createPetBody.setName("Murzik");
//        // // Rewrite it after 9999 year
//        createPetBody.setBirthdate(Date.valueOf("9999-01-01"));
//        createPetBody.setBreed("Siamese");
//        createPetBody.setColor("black");
//
//        when(petService.createPet(any(), any(), any(), any())).thenReturn(1L);
//        mockMvc.perform(post("/pets")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createPetBody)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testReadPet() throws Exception {
//        PetDto petDto = PetDto.builder()
//                .id(1L)
//                .name("Barsik")
//                .birthdate(Date.valueOf("2025-01-01"))
//                .build();
//        when(petService.getPetDtoById(anyLong())).thenReturn(petDto);
//
//        mockMvc.perform(get("/pets/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1));
//    }
//
//    @Test
//    void testReadNonExistingPet() throws Exception {
//        when(petService.getPetDtoById(anyLong())).thenThrow(new EntityNotFound(1L));
//
//        mockMvc.perform(get("/pets/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testDeletePet() throws Exception {
//        mockMvc.perform(delete("/pets/1"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testGetFilteredPets() throws Exception {
//        PetDto petDto1 = PetDto.builder()
//                .id(1L)
//                .name("Barsik")
//                .birthdate(Date.valueOf("2025-01-01"))
//                .breed("Siamese")
//                .color(AvailableColor.black)
//                .build();
//        PetDto petDto2 = PetDto.builder()
//                .id(2L)
//                .name("Murka")
//                .birthdate(Date.valueOf("2025-02-01"))
//                .breed("Persian")
//                .color(AvailableColor.white)
//                .build();
//        when(petService.getByParams(any(), any(), any(), anyInt(), anyInt())).thenReturn(List.of(petDto1, petDto2));
//
//        mockMvc.perform(get("/pets")
//                        .param("name", "")
//                        .param("breed", "")
//                        .param("colors", "")
//                        .param("size", "5")
//                        .param("page", "0"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    @Test
//    void testGetFilteredPetsWithWrongArguments() throws Exception {
//        mockMvc.perform(get("/pets")
//                        .param("name", "")
//                        .param("breed", "")
//                        .param("colors", "")
//                        .param("size", "0")
//                        .param("page", "0"))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(get("/pets")
//                        .param("name", "")
//                        .param("breed", "")
//                        .param("colors", "")
//                        .param("size", "-1")
//                        .param("page", "0"))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(get("/pets")
//                        .param("name", "")
//                        .param("breed", "")
//                        .param("colors", "")
//                        .param("size", "1")
//                        .param("page", "-1"))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(get("/pets")
//                        .param("name", "")
//                        .param("breed", "")
//                        .param("colors", "notColor")
//                        .param("size", "1")
//                        .param("page", "0"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testAddPetFriend() throws Exception {
//        FriendPairBody friendPairBody = new FriendPairBody(1L, 2L);
//        mockMvc.perform(put("/pets/friends")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(friendPairBody)))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testAddSamePetFriend() throws Exception {
//        FriendPairBody friendPairBody = new FriendPairBody(1L, 2L);
//        mockMvc.perform(put("/pets/friends")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(friendPairBody)))
//                .andExpect(status().isNoContent());
//        mockMvc.perform(put("/pets/friends")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(friendPairBody)))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testRemovePetFriend() throws Exception {
//        FriendPairBody friendPairBody = new FriendPairBody(1L, 2L);
//        mockMvc.perform(delete("/pets/friends")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(friendPairBody)))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testRemoveSamePetFriend() throws Exception {
//        FriendPairBody friendPairBody = new FriendPairBody(1L, 2L);
//        mockMvc.perform(delete("/pets/friends")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(friendPairBody)))
//                .andExpect(status().isNoContent());
//        mockMvc.perform(delete("/pets/friends")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(friendPairBody)))
//                .andExpect(status().isNoContent());
//    }
//}
