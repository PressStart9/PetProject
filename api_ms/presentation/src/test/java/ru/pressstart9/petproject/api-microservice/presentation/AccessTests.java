package ru.pressstart9.petproject.api-microservice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pressstart9.petproject.dto.requests.CreatePersonBody;
import ru.pressstart9.petproject.dto.requests.CreatePetBody;
import ru.pressstart9.petproject.dto.requests.FriendPairBody;
import ru.pressstart9.petproject.dto.responses.PetDto;
import ru.pressstart9.petproject.presentation.controllers.AuthController;
import ru.pressstart9.petproject.presentation.controllers.PersonController;
import ru.pressstart9.petproject.presentation.controllers.PetController;
import ru.pressstart9.petproject.service.AuthService;
import ru.pressstart9.petproject.service.PersonService;
import ru.pressstart9.petproject.service.PetService;
import ru.pressstart9.petproject.service.UserInfoService;
import ru.pressstart9.petproject.service.util.ExtendedUser;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { AuthController.class, PersonController.class, PetController.class })
public class AccessTests {
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

    private ExtendedUser mockUser(Long id, String role) {
        return new ExtendedUser(id, "email", "password", List.of(() -> role));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"user"})
    void testUserCreatePersonForbidden() throws Exception {
        CreatePersonBody body = PersonControllerAdminTests.getValidCreatePersonBody();

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testAnonymousUnauthorizedAccess() throws Exception {
        mockMvc.perform(post("/people"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/people/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/people/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/pets/"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/pets/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/pets/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/pets/friends"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/pets/friends"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteSelfPerson() throws Exception {
        ExtendedUser user = mockUser(5L, "user");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));

        mockMvc.perform(delete("/people/5"))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/people/6"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testManagePetsOwner() throws Exception {
        ExtendedUser user = mockUser(5L, "user");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));

        CreatePetBody createPetBody = new CreatePetBody("A", new Date());
        when(petService.getPetDtoById(1L)).thenReturn(PetDto.builder().ownerId(5L).build());
        when(petService.getPetDtoById(2L)).thenReturn(PetDto.builder().ownerId(2L).build());

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/pets/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetBody)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testManagePetFriendsOwner() throws Exception {
        ExtendedUser user = mockUser(5L, "user");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));

        FriendPairBody friendPairBody = new FriendPairBody(5L, 6L);
        FriendPairBody wrongFriendPairBody = new FriendPairBody(7L, 8L);
        when(petService.getPetDtoById(5L)).thenReturn(PetDto.builder().ownerId(5L).build());
        when(petService.getPetDtoById(7L)).thenReturn(PetDto.builder().ownerId(7L).build());

        mockMvc.perform(put("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendPairBody)))
                .andExpect(status().isNoContent());
        mockMvc.perform(put("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongFriendPairBody)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendPairBody)))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/pets/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongFriendPairBody)))
                .andExpect(status().isForbidden());
    }
}
