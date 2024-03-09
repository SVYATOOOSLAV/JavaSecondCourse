package edu.java.bot.model.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {

    String commandName();
    String description();
    SendMessage handle(long chatID, String text);
    default BotCommand toApiCommand() {
        return new BotCommand(commandName(), description());
    }
}
