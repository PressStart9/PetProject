package ru.pressstart9.petproject.service;

import jakarta.persistence.NonUniqueResultException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pressstart9.petproject.commons.exceptions.EmailNotUnique;
import ru.pressstart9.petproject.dao.PersonRepository;
import ru.pressstart9.petproject.dao.UserInfoRepository;
import ru.pressstart9.petproject.domain.Person;
import ru.pressstart9.petproject.domain.UserInfo;
import ru.pressstart9.petproject.service.util.ExtendedUser;

import java.util.Optional;
import java.util.Set;

@Service
public class UserInfoService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoService(UserInfoRepository userInfoRepository, PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(userInfo.getRole().toString()));

        return new ExtendedUser(
                userInfo.getPerson() == null ? null : userInfo.getPerson().getId(),
                email,
                userInfo.getHashedPassword(),
                authorities
        );
    }

    public void createUserInfo(String email, String password, Long personId) {
        try {
            Optional<Person> person = personId == null ? Optional.empty() : personRepository.findById(personId);
            userInfoRepository.save(new UserInfo(email, passwordEncoder.encode(password),
                    person.orElse(null)));
        } catch (NonUniqueResultException e) {
            throw new EmailNotUnique(e.getMessage());
        }
    }
}
