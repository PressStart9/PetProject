package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "Pets")
@NoArgsConstructor @AllArgsConstructor
@Getter
public class Pet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String breed;

    @Enumerated
    private AvailableColor color;

    @ManyToOne
    private Person owner;
}
