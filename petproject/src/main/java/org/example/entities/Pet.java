package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "Pets")
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
public class Pet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String breed;

    @Enumerated
    private AvailableColor color;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Person owner;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Pet> friends = new HashSet<>();
}
