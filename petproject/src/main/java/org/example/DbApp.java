package org.example;

import lombok.val;
import org.example.dao.DaoFactory;

public class DbApp {
    public static void main(String[] args) {
        val dao = new DaoFactory();
        val petRepo = dao.getPetRepository();

//        petRepo.save(new Pet(null, "dog", "smth", AvailableColor.Black, null));
//        petRepo.save(new Pet(null, "cat", "smth", AvailableColor.White, null));
    }
}
