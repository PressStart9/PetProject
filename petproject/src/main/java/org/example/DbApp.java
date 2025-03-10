package org.example;

import lombok.val;

public class DbApp {
    public static void main(String[] args) {
        val petRepo = DaoFactory.get().getPetRepository();

        petRepo.deleteAll();
    }
}
