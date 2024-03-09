package edu.java.controllers;

import edu.java.model.dto.LinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinksApiController implements LinksApi{
    @Override
    public ResponseEntity<?> addLinkToUser(long id, LinkRequest link) {
        return null;
    }

    @Override
    public ResponseEntity<?> getLinksFromUser(long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteLinkFromUser(long id, LinkRequest link) {
        return null;
    }
}
