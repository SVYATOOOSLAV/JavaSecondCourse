package edu.java.model;

import edu.java.controllers.exceptions.ApiException;
import edu.java.dao.DateBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.List;
import java.util.Random;
import static edu.java.dao.DateBase.DATABASE;

@Service
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBaseConnectionService {
    public void createUser(Long userID){
        validID(userID);
        DateBase.createUser(userID);
        log.info("User with id " + userID + " was created");
    }

    public  void addURIToUser(Long userID, URI link){
        validID(userID);
        userInSystem(userID);
        DateBase.addURIToUser(userID, link);
        log.info(link + " added to user with ID " + userID);
    }

    public void removeURIFromUser(Long userID, URI link){
        validID(userID);
        userInSystem(userID);
        uriInList(userID, link);
        DateBase.removeURIFromUser(userID, link);
        log.info(link + " removed from user with ID " + userID);
    }

    public void deleteUser(Long userID){
        if (!DateBase.isUserInSystem(userID)) {
            throw new ApiException("User not in system");
        }
        DateBase.clearListForUser(userID);
        log.info("User with id " + userID + " was deleted");
    }
    public List<URI> getListOfLinks(long userId) {
        validID(userId);
        userInSystem(userId);
        return DateBase.getUserTrackList(userId);
    }

    private void validID(long id){
        if (id < 0) {
            throw new ApiException("Uncorrected ID < 0");
        }
    }

    private void userInSystem(long id){
        if(!DateBase.isUserInSystem(id)){
            throw new ApiException("User with ID" + id + "doesn't exist");
        }
    }

    private void uriInList(long id, URI link){
        if(DateBase.getUserTrackList(id).stream().noneMatch(uri -> uri.equals(link))){
            throw new ApiException(link + " does not exists in user with id " + id);
        }
    }
}
