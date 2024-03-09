package edu.java.bot.model.linkvalidators;

import java.net.URI;

public interface LinkValidator {
    String hostName();
    default boolean isValidLink(URI link){
        return link.getHost() != null && link.getHost().equals(hostName()) && !link.getPath().isEmpty();
    }
}
