package ru.pressstart9.petproject.api_ms.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pressstart9.petproject.commons.UserRole;

@Entity @Table(name = "user_info")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserInfo {
    public UserInfo(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public UserInfo(String email, String hashedPassword, Long personId) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.personId = personId;
    }

    @Id
    @Column(unique = true, nullable = false)
    private String email;

    private String hashedPassword;

    @Enumerated
    private UserRole role = UserRole.user;

    private Long personId = null;
}
