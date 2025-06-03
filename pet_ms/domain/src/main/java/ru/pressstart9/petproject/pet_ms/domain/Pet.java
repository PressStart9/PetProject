package ru.pressstart9.petproject.pet_ms.domain;

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
    public Pet(String name, Date birthdate, String breed, AvailableColor color, Long ownerId) {
        this.name = name;
        this.birthdate = birthdate;
        this.breed = breed;
        this.color = color;
        this.ownerId = ownerId;
    }

    @EqualsAndHashCode.Include
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;
    public Date birthdate;
    public String breed;

    @Enumerated
    public AvailableColor color;

    public Long ownerId;

    @ManyToMany(fetch = FetchType.LAZY)
    public Set<Pet> friends = new HashSet<>();

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
