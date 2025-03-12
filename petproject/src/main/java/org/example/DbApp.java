package org.example;

import lombok.val;
import org.example.entities.AvailableColor;
import org.example.entities.Pet;

public class DbApp {
    public static void main(String[] args) {
        val dao = new DaoFactory();
        val petRepo = dao.getPetRepository();

//        petRepo.save(new Pet(null, "dog", "smth", AvailableColor.Black, null));
//        petRepo.save(new Pet(null, "cat", "smth", AvailableColor.White, null));
    }
}
