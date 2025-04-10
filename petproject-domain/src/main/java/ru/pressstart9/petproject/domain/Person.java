package ru.pressstart9.petproject.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "person")
@Getter @Setter
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

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Pet> pets = new HashSet<>();

    public boolean addPet(Pet pet) {
        if (pet.getOwner() != null) {
            return false;
        }
        pet.setOwner(this);
        pets.add(pet);
        return true;
    }

    public void removePet(Pet pet) {
        pet.setOwner(null);
        pets.remove(pet);
    }
}
