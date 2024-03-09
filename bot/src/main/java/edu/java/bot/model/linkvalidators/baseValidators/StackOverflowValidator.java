package edu.java.bot.model.linkvalidators.baseValidators;

import edu.java.bot.model.linkvalidators.LinkValidator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StackOverflowValidator implements LinkValidator {
    @Override
    public String hostName() {
        return "stackoverflow.com";
    }
}
