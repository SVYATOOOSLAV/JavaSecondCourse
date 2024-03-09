package edu.java.controllers;

import edu.java.model.DataBaseConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserApiController implements UserApi {
    private DataBaseConnectionService service;
    @Autowired
    public UserApiController(DataBaseConnectionService service){
        this.service = service;
    }
    @Override
    public ResponseEntity<?> createUserInSystem(@PathVariable("id") long id) {
        service.createUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteUserFromSystem(@PathVariable("id") long id) {
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
