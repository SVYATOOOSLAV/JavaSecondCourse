package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import lombok.Getter;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CommandProcessor implements UserMessageProcessor {

    @Getter
    private List<Command> commands;

    @Autowired
    public CommandProcessor(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void addCommand(Command command) {
        commands.add(command);
    }

    @Override
    public void removeCommand(String commandName) {
        commands = commands.stream().filter(command -> !command.commandName().equals(commandName)).toList();
    }

    @Override
    public SendMessage process(Long userID, String text) {
        for (Command command : commands) {
            if (text.startsWith(command.commandName())) {
                return command.handle(userID, text);
            }
        }
        return new SendMessage(userID, "Command not found, send me command /help");
    }
}
