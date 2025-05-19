package ru.pressstart9.petproject.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pressstart9.petproject.dto.auth.CreateAccountBody;
import ru.pressstart9.petproject.dto.auth.LoginBody;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final UserInfoService userInfoService;
    private final PersonService personService;

    public AuthService(AuthenticationManager authenticationManager, UserInfoService userInfoService, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.userInfoService = userInfoService;
        this.personService = personService;
    }

    @Transactional
    public Authentication createAccount(CreateAccountBody createAccountBody) {
        Long personId = personService.createPerson(createAccountBody.getName(), createAccountBody.getBirthdate());
        userInfoService.createUserInfo(createAccountBody.getEmail(), createAccountBody.getPassword(), personId);

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
