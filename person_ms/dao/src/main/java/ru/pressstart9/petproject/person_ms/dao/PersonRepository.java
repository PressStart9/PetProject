package ru.pressstart9.petproject.person_ms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pressstart9.petproject.person_ms.domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
