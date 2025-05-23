package ru.pressstart9.petproject.person_ms.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "person")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
    public Person(String name, Date birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }

    @EqualsAndHashCode.Include
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date birthdate;

    private Set<Long> petIds = new HashSet<>();

    public void addPet(Long petId) {
        petIds.add(petId);
    }

    public void removePet(Long petId) {
        petIds.remove(petId);
    }
}
