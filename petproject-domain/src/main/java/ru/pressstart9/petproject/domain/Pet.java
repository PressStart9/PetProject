package ru.pressstart9.petproject.domain;

import jakarta.persistence.*;
import lombok.*;
import ru.pressstart9.petproject.commons.AvailableColor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "pet")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pet {
    public Pet(String name, Date birthdate, String breed, AvailableColor color) {
        this.name = name;
        this.birthdate = birthdate;
        this.breed = breed;
        this.color = color;
    }

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

    public void addFriend(Pet friend) {
        if (this == friend) {
            return;
        }
        friend.friends.add(this);
        friends.add(friend);
    }

    public void removeFriend(Pet friend) {
        friends.remove(friend);
        friend.friends.remove(this);
    }
}
