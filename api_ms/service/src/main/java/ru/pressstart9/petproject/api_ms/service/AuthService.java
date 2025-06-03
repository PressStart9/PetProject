package ru.pressstart9.petproject.api_ms.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.pressstart9.petproject.commons.dto.auth.CreateAccountBody;
import ru.pressstart9.petproject.commons.dto.auth.LoginBody;
import ru.pressstart9.petproject.commons.dto.requests.CreatePersonBody;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final UserInfoService userInfoService;
    private final RequestProducer requestProducer;

    public AuthService(AuthenticationManager authenticationManager, UserInfoService userInfoService, RequestProducer requestProducer) {
        this.authenticationManager = authenticationManager;
        this.userInfoService = userInfoService;
        this.requestProducer = requestProducer;
    }

    public Authentication createAccount(CreateAccountBody createAccountBody) {
        var response = requestProducer.sendPersonRequest(
                new CreatePersonBody(createAccountBody.getName(), createAccountBody.getBirthdate()));
        userInfoService.createUserInfo(createAccountBody.getEmail(), createAccountBody.getPassword(), response.id);
        return login(new LoginBody(createAccountBody.getEmail(), createAccountBody.getPassword()));
    }

    public Authentication login(LoginBody loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}
