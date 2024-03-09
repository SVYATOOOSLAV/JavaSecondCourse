package edu.java.bot.controllers;

import edu.java.DTO.request.LinkUpdate;
import edu.java.bot.model.LinkUpdatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateAPIController implements UpdateAPI{
    private final LinkUpdatesService service;
    @Override
    public ResponseEntity<?> sendUpdate(LinkUpdate update) {
        service.sendUpdates(update);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
