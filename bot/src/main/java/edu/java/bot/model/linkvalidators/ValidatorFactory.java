package edu.java.bot.model.linkvalidators;

public class ValidatorFactory {
    public static LinkValidator create (String hostName){
        return new LinkValidator() {
            @Override
            public String hostName() {
                return hostName;
            }
        };
    }
}
