package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.PersonDto;
import org.example.dto.PetDto;
import org.example.dto.AvailableColor;
import org.example.service.PersonService;
import org.example.service.PetService;

import java.util.List;

@AllArgsConstructor
public class DummyPresentation {
    private final PetService petService;
    private final PersonService personService;

    public Long createPerson(PersonDto personDto) {
        return personService.createPerson(personDto);
    }

    public PersonDto getPersonById(long id) {
        return personService.getPersonById(id);
    }

    public List<PersonDto> getAllPeople() {
        return personService.getAllPeople();
    }

    public void updatePerson(PersonDto personDto) {
        personService.updatePerson(personDto);
    }

    public void deletePersonById(long id) {
        personService.deletePersonById(id);
    }

    public void deleteAllPeople() {
        personService.deleteAllPeople();
    }

    public List<PetDto> getPetsByColor(AvailableColor color) {
        return petService.getPetsByColor(color);
    }

    public Long createPet(PetDto petDto) {
        return petService.createPet(petDto);
    }

    public PetDto getPetById(long id) {
        return petService.getPetById(id);
    }

    public List<PetDto> getAllPets() {
        return petService.getAllPets();
    }

    public void updatePet(PetDto petDto) {
        petService.updatePet(petDto);
    }

    public void deletePetById(long id) {
        petService.deletePetById(id);
    }

    public void deleteAllPets() {
        petService.deleteAllPets();
    }
}
