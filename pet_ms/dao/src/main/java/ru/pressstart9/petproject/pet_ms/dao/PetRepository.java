package ru.pressstart9.petproject.pet_ms.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pressstart9.petproject.common_kafka.AvailableColor;
import ru.pressstart9.petproject.pet_ms.domain.Pet;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT p FROM Pet p WHERE (:name IS NULL OR p.name = :name) " +
            "AND (:breed IS NULL OR p.breed = :breed) " +
            "AND (:color IS NULL OR p.color IN :color)")
    List<Pet> findByParams(@Param("name") String name,
                           @Param("breed") String breed,
                           @Param("color") List<AvailableColor> color,
                           Pageable pageable);
}
