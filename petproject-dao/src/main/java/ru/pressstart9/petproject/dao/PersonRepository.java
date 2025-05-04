package ru.pressstart9.petproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pressstart9.petproject.domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
