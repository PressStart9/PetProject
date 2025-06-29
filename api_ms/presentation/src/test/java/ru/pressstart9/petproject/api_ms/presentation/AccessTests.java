package ru.pressstart9.petproject.api_ms.presentation;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pressstart9.petproject.api_ms.service.AuthService;
import ru.pressstart9.petproject.api_ms.service.UserInfoService;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.commons.dto.requests.CreatePersonBody;
import ru.pressstart9.petproject.commons.dto.requests.CreatePetBody;
import ru.pressstart9.petproject.commons.dto.requests.FriendPairBody;
import ru.pressstart9.petproject.commons.dto.requests.GetRequest;
import ru.pressstart9.petproject.commons.dto.responses.CreatedResponse;
import ru.pressstart9.petproject.commons.dto.responses.PetDto;
import ru.pressstart9.petproject.api_ms.presentation.controllers.AuthController;
import ru.pressstart9.petproject.api_ms.presentation.controllers.PersonController;
import ru.pressstart9.petproject.api_ms.presentation.controllers.PetController;
import ru.pressstart9.petproject.api_ms.service.util.ExtendedUser;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = { AuthController.class, PersonController.class, PetController.class })
@ContextConfiguration(classes = {TestApplication.class})
public class AccessTests {
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
        when(requestProducer.sendPetRequest(any(CreatePetBody.class))).thenReturn(new CreatedResponse(1L));
        when(requestProducer.sendPetRequest(new GetRequest(1L))).thenReturn(PetDto.builder().ownerId(5L).build());
        when(requestProducer.sendPetRequest(new GetRequest(2L))).thenReturn(PetDto.builder().ownerId(2L).build());

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
        when(requestProducer.sendPetRequest(new GetRequest(5L))).thenReturn(PetDto.builder().ownerId(5L).build());
        when(requestProducer.sendPetRequest(new GetRequest(6L))).thenReturn(PetDto.builder().ownerId(7L).build());
        when(requestProducer.sendPetRequest(new GetRequest(7L))).thenReturn(PetDto.builder().ownerId(1L).build());
        when(requestProducer.sendPetRequest(new GetRequest(8L))).thenReturn(PetDto.builder().ownerId(1L).build());

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
