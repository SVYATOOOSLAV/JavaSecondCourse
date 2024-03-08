package edu.java.controllers;

import edu.java.dao.DataBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Test {
    public static void main(String[] args) {
        UserController userController = new UserController();
        HttpStatus response = userController.createUserInSystem(12);

        System.out.println(DataBase.isUserInSystem(12L));
        System.out.println(response.value());
    }
}
