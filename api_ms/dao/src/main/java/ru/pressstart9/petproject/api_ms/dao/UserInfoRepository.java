package ru.pressstart9.petproject.api_ms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pressstart9.petproject.commons.UserRole;
import ru.pressstart9.petproject.api_ms.domain.UserInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    Optional<UserInfo> findByEmail(String email);
    List<UserInfo> findByRole(UserRole role);
}
