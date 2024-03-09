package edu.java.bot.controllers;

import edu.java.DTO.request.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/updates")
public interface UpdateAPI {
    @PostMapping(consumes = "application/json")
    ResponseEntity<?> sendUpdate(@RequestBody LinkUpdate update);
}
