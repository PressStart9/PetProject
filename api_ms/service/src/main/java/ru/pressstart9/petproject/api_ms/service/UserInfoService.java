package ru.pressstart9.petproject.api_ms.service;

import jakarta.persistence.NonUniqueResultException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pressstart9.petproject.api_ms.service.util.ExtendedUser;
import ru.pressstart9.petproject.commons.exceptions.EmailNotUnique;
import ru.pressstart9.petproject.api_ms.dao.UserInfoRepository;
import ru.pressstart9.petproject.api_ms.domain.UserInfo;

import java.util.Optional;
import java.util.Set;

@Service
public class UserInfoService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoService(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(userInfo.getRole().toString()));

        return new ExtendedUser(
                userInfo.getPersonId(),
                email,
                userInfo.getHashedPassword(),
                authorities
        );
    }

    public void createUserInfo(String email, String password, Long personId) {
        try {
            userInfoRepository.save(new UserInfo(email, passwordEncoder.encode(password),
                    personId));
        } catch (NonUniqueResultException e) {
            throw new EmailNotUnique(e.getMessage());
        }
    }

    public void deleteUserInfo(Long personId) {
        Optional<UserInfo> userInfo = userInfoRepository.findByPersonId(personId);
        userInfo.ifPresent(userInfoRepository::delete);
    }
}
