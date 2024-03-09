package edu.java.bot.model.commands;

import com.pengrad.telegrambot.request.SendMessage;

@FunctionalInterface
public interface CommandHandler {
    SendMessage handle(Long userID, String text);
}
