package ru.pressstart9.petproject.api_ms.service.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.commons.UserRole;
import ru.pressstart9.petproject.dao.UserInfoRepository;
import ru.pressstart9.petproject.domain.UserInfo;

import java.util.List;

@Component
public class AdminProvider implements CommandLineRunner {
    private final Environment environment;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    public AdminProvider(Environment environment, UserInfoRepository userRepository, PasswordEncoder passwordEncoder) {
        this.environment = environment;
        this.userInfoRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws RuntimeException {
        String email = environment.getProperty("app.admin-email");
        String password = environment.getProperty("app.admin-password");
        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            throw new RuntimeException("Environment variable ADMIN_EMAIL or ADMIN_PASSWORD is blank.");
        }

        List<UserInfo> admin = userInfoRepository.findByRole(UserRole.admin);
        if (!admin.isEmpty()) {
            if (admin.stream().anyMatch(u -> u.getEmail().equals(email))) {
                return;
            }
            throw new RuntimeException("Environment variable ADMIN_EMAIL differs from existing admins.");
        }
        UserInfo newAdmin = new UserInfo(email, passwordEncoder.encode(password));
        newAdmin.setRole(UserRole.admin);

        userInfoRepository.save(newAdmin);
    }
}
