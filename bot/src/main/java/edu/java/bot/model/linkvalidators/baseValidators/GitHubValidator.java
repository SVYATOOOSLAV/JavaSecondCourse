package edu.java.bot.model.linkvalidators.baseValidators;

import edu.java.bot.model.linkvalidators.LinkValidator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GitHubValidator implements LinkValidator {
    @Override
    public String hostName() {
        return "github.com";
    }
}
