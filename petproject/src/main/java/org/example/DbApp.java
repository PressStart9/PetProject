package org.example;

import lombok.val;

public class DbApp {
    public static void main(String[] args) {
        val dao = new DaoFactory();
        val petRepo = dao.getPetRepository();

        petRepo.deleteAll();
    }
}
