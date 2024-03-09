package edu.java.bot.model.commands;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommandFactory {
    public static Command create (String commandName, String description, CommandHandler handler){
        return  new Command() {
            @Override
            public String commandName() {
                return commandName;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public SendMessage handle(long chatID, String text) {
                return handler.handle(chatID, text);
            }
        };
    }

    public static Command create (String commandName, CommandHandler handler){
        return  new Command() {
            @Override
            public String commandName() {
                return commandName;
            }

            @Override
            public String description() {
                return "";
            }

            @Override
            public SendMessage handle(long chatID, String text) {
                return handler.handle(chatID, text);
            }
        };
    }
}
