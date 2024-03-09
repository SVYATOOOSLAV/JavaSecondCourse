package edu.java.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/chat")
public interface UserApi {
    @PostMapping("/{id}")
    ResponseEntity<?> createUserInSystem(@PathVariable("id") long id);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUserFromSystem(@PathVariable("id") long id);
}
