package org.example.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity @Table(name = "People")
public class Person {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date date;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Pet> pets;
}
