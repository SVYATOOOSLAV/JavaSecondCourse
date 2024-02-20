package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;

@FunctionalInterface
public interface CommandHandler {
    SendMessage handle(Long userID, String text);
}
