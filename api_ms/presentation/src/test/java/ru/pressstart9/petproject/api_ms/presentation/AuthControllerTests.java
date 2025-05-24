package ru.pressstart9.petproject.api_ms.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.commons.dto.auth.CreateAccountBody;
import ru.pressstart9.petproject.commons.dto.auth.LoginBody;
import ru.pressstart9.petproject.api_ms.presentation.controllers.AuthController;
import ru.pressstart9.petproject.api_ms.service.AuthService;
import ru.pressstart9.petproject.api_ms.service.UserInfoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {TestApplication.class})
public class AuthControllerTests {
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

    public static CreateAccountBody getValidCreateAccountBody() {
        try {
            return new CreateAccountBody("example@example.com", "12345678", "Ivan", new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse("2025-01-01"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static LoginBody getValidLoginBody() {
        return new LoginBody("example@example.com", "12345678");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePerson() throws Exception {
        when(authService.createAccount(any())).thenReturn(new UsernamePasswordAuthenticationToken(null, null));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getValidCreateAccountBody())))
                .andExpect(status().isOk());
    }

    @Test
    void testCreatePersonWithInvalidPassword() throws Exception {
        CreateAccountBody createAccountBody = getValidCreateAccountBody();
        createAccountBody.setPassword("1234567"); // Short password

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());

        createAccountBody.setPassword(null);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateBlankPerson() throws Exception {
        CreateAccountBody createAccountBody = getValidCreateAccountBody();
        createAccountBody.setName("   ");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());

        createAccountBody.setName(null);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePersonWithInvalidEmail() throws Exception {
        CreateAccountBody createAccountBody = getValidCreateAccountBody();
        createAccountBody.setEmail("exampleaexample.com");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());

        createAccountBody.setEmail(null);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFuturePerson() throws Exception {
        CreateAccountBody createAccountBody = getValidCreateAccountBody();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        createAccountBody.setBirthdate(calendar.getTime());

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin() throws Exception {
        when(authService.login(any())).thenReturn(new UsernamePasswordAuthenticationToken(null, null));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getValidLoginBody())))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginWithInvalidPassword() throws Exception {
        LoginBody loginBody = getValidLoginBody();
        loginBody.setPassword(null);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithInvalidEmail() throws Exception {
        LoginBody loginBody = getValidLoginBody();
        loginBody.setEmail(null);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isBadRequest());

        loginBody.setEmail("example@@com");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isBadRequest());
    }
}
