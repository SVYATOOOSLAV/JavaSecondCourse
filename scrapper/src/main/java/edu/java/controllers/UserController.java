package edu.java.controllers;

import edu.java.controllers.exceptions.ScrapperException;
import edu.java.dao.DataBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@Slf4j
public class UserController {

    @PostMapping("/{id}")
    public HttpStatus createUserInSystem(@PathVariable("id") long id){
        isValidID(id);
        DataBase.createUser(id);
        log.info("User with id " + id + " was created");
        return HttpStatus.OK;
    }

    private void isValidID(long id){
        if(id < 0){
            throw new ScrapperException("Uncorrected ID");
        }
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteUserFromSystem(@PathVariable("id") long id){
        if(!DataBase.isUserInSystem(id)){
            throw new ScrapperException("User not in system");
        }
        DataBase.clearListForUser(id);
        log.info("User with id " + id + " was deleted");
        return HttpStatus.OK;
    }

}
