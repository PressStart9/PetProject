package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "Pets")
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pet {
    @EqualsAndHashCode.Include
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date birthdate;
    private String breed;

    @Enumerated
    private AvailableColor color;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person owner;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Pet> friends = new HashSet<>();
}
