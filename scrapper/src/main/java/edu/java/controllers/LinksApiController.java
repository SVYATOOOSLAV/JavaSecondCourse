package edu.java.controllers;

import edu.java.DTO.request.LinkRequest;
import edu.java.DTO.response.LinkResponse;
import edu.java.DTO.response.ListLinksResponse;
import edu.java.model.DataBaseConnectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@AllArgsConstructor
public class LinksApiController implements LinksApi {
    private final DataBaseConnectionService service;
    @Override
    public ResponseEntity<LinkResponse> addLinkToUser(long id, LinkRequest link) {
        service.addLinkToUser(id, link.link());
        return new ResponseEntity<>(new LinkResponse(HttpStatus.OK.value(), URI.create(link.link())),
            HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinksFromUser(long id) {
       return new ResponseEntity<>(service.getListOfLinks(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LinkResponse> deleteLinkFromUser(long id, LinkRequest link) {
        service.removeLinkFromUser(id, link.link());
        return new ResponseEntity<>(new LinkResponse(HttpStatus.OK.value(), URI.create(link.link())),
            HttpStatus.OK);
    }
}
