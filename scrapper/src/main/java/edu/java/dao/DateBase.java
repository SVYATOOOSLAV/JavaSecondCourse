package edu.java.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateBase {
    private final static HashMap<Long, List<URI>> DATABASE = new HashMap<>();
    public static void createUser(Long userID){
        DATABASE.put(userID, new ArrayList<>());
    }
    public static void addLinkToUser(Long userID, URI link){
        List<URI> resources = DATABASE.get(userID);
        resources.add(link);
        DATABASE.put(userID, resources);
    }

    public static void addLinksToUser (Long userID, List<URI> newLinks)
    {
        List<URI> previousTrackList = DATABASE.get(userID);
        previousTrackList.addAll(newLinks);
        DATABASE.put(userID, previousTrackList);
    }
    public static void removeLinkFromUser(Long userID, URI link){
        List<URI> resources = DATABASE.get(userID);
        resources.remove(link);
        DATABASE.put(userID, resources);
    }
    public static void removeLinksFromUser(Long userID, List<URI> oldLinks){
        List<URI> currentlyLinks = DATABASE.get(userID);
        currentlyLinks.removeAll(oldLinks);
        DATABASE.put(userID, currentlyLinks);
    }

    public static void deleteUser(Long userID){
        DATABASE.remove(userID);
    }

    public static void dropDB(){
        DATABASE.clear();
    }

    public static boolean isUserInSystem(Long userID){
        return DATABASE.containsKey(userID);
    }

    public static List<URI> getUserTrackList(Long userID){
        return DATABASE.get(userID);
    }

}
