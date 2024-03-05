package edu.java.bot.linkvalidators.baseValidators;

import edu.java.bot.linkvalidators.LinkValidator;
import lombok.NoArgsConstructor;

import java.net.URI;

@NoArgsConstructor
public class GitHubValidator implements LinkValidator {
    @Override
    public String hostName() {
        return "github.com";
    }
}
