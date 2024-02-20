package edu.java.bot.linkvalidators;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public final class LinkValidatorManager {
    private List<LinkValidator> validators;

    public List<LinkValidator> getValidators() {
        return validators;
    }

    public LinkValidatorManager(List<LinkValidator> validators) {
        this.validators = validators;
    }

    public boolean isValidLink(URI link) {
        String fullPath = getFullPath(link);
        URI uri = URI.create(fullPath);
        return validators.stream()
            .anyMatch(validator -> validator.isValidLink(uri));
    }

    private String getFullPath(URI link) {
        String scheme = "https://";
        String fullPath = link.toString();
        if (!fullPath.startsWith(scheme)) {
            fullPath = scheme + fullPath;
        }
        return fullPath;
    }
}
