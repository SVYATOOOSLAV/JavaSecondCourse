package edu.java.bot.model.processor;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.commands.Command;
import java.util.List;

public interface UserMessageProcessor {
    List<Command> getCommands();

    SendMessage process(Long chatID, String text);

    void addCommand(Command command);
    void removeCommand(String commandName);
}
