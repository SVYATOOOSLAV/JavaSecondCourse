package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;

public interface UserMessageProcessor {
    List<Command> getCommands();

    SendMessage process(Long chatID, String text);

    void addCommand(Command command);
    void removeCommand(String commandName);
}
