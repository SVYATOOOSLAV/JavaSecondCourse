package edu.java.controllers;

import edu.java.model.dto.LinkRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("chat/{id}/links")
public interface LinksApi {
    @PostMapping(value = "/content", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addLinkToUser(@PathVariable("id") long id, @RequestParam LinkRequest link);

    @GetMapping
    ResponseEntity<?> getLinksFromUser(@PathVariable("id") long id);

    @DeleteMapping(value = "/content", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteLinkFromUser(@PathVariable("id") long id, @RequestParam LinkRequest link);
}
