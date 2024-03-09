package edu.java.model;


import edu.java.DTO.exception.DataNotFoundException;
import edu.java.DTO.exception.InvalidRequestException;
import edu.java.DTO.response.LinkResponse;
import edu.java.DTO.response.ListLinksResponse;
import edu.java.dao.DateBase;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBaseConnectionService {
    public void createUser(Long userID){
        idValidator(userID);
        userNotInSystemValidator(userID);
        DateBase.createUser(userID);
        log.info("User with id " + userID + " was created");
    }

    public  void addLinkToUser(Long userID, String link){
        idValidator(userID);
        userInSystemValidator(userID);
        linkValidator(link);
        uriIsNotInList(userID, link);
        DateBase.addLinkToUser(userID, URI.create(link));
        log.info(link + " added to user with ID " + userID);
    }

    public void removeLinkFromUser(Long userID, String link){
        idValidator(userID);
        userInSystemValidator(userID);
        linkValidator(link);
        uriInList(userID, link);
        DateBase.removeLinkFromUser(userID, URI.create(link));
        log.info(link + " removed from user with ID " + userID);
    }

    public void deleteUser(Long userID){
        idValidator(userID);
        userInSystemValidator(userID);
        DateBase.deleteUser(userID);
        log.info("User with id " + userID + " was deleted");
    }
    public ListLinksResponse getListOfLinks(long userId) {
        idValidator(userId);
        userInSystemValidator(userId);
        return new ListLinksResponse(
            DateBase.getUserTrackList(userId)
                .stream()
                .map(uri -> new LinkResponse(HttpStatus.OK.value(), uri))
                .toArray(LinkResponse[]::new)
        );
    }

    private void idValidator(long id){
        if (id < 0) {
            throw new InvalidRequestException("Uncorrected ID < 0");
        }
    }

    private void userNotInSystemValidator(long id){
        if(DateBase.isUserInSystem(id)){
            throw new InvalidRequestException ("User with ID" + id + "already exist");
        }
    }

    private void userInSystemValidator(long id){
        if(!DateBase.isUserInSystem(id)){
            throw new DataNotFoundException("User with ID" + id + "doesn't exist");
        }
    }

    private void linkValidator(String link){
        try {
            new URI(link);
        } catch (URISyntaxException e) {
            throw new InvalidRequestException(link + "is invalid");
        }
    }

    private void uriInList(long id, String link){
        if(DateBase.getUserTrackList(id).stream()
                .map(URI::toString).noneMatch(uri -> uri.equals(link))){
            throw new DataNotFoundException(link + " does not exists in user with id " + id);
        }
    }

    private void uriIsNotInList(long id, String link){
        if(DateBase.getUserTrackList(id).stream()
            .map(URI::toString).anyMatch(uri -> uri.equals(link))){
            throw new InvalidRequestException (link + " already exist in user with id: " + id);
        }
    }
}
