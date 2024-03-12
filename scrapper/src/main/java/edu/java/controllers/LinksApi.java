package edu.java.controllers;

import edu.java.DTO.request.LinkRequest;
import edu.java.DTO.response.LinkResponse;
import edu.java.DTO.response.ListLinksResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/links")
public interface LinksApi {
    @GetMapping(produces = "application/json")
    ResponseEntity<ListLinksResponse> getLinksFromUser(@RequestHeader("Tg-Chat-Id") long id);

    @PostMapping(produces = "application/json", consumes = "application/json")
    ResponseEntity<LinkResponse> addLinkToUser(@RequestHeader("Tg-Chat-Id")  long id, @RequestBody LinkRequest link);

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    ResponseEntity<LinkResponse> deleteLinkFromUser(@RequestHeader("Tg-Chat-Id")  long id, @RequestBody LinkRequest link);
}
