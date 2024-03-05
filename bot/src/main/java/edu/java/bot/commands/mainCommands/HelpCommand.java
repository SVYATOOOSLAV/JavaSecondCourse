package edu.java.bot.commands.mainCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.dao.DataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class HelpCommand implements Command {
    private final List<Command> commands;

    @Autowired
    public HelpCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String commandName() {
        return "/help";
    }

    @Override
    public String description() {
        return "Список команд";
    }

    @Override
    public SendMessage handle(long chatID, String text) {
        if (!DataBase.isUserInSystem(chatID)) {
            return new SendMessage(chatID, "Вас нет в системе!\nНапишите команду /start");
        }

        if (text.equals(commandName())) {
            StringBuilder sb = new StringBuilder("Список существующих команд:\n");
            commands.forEach(command -> {
                sb.append(command.commandName()).
                    append(": ")
                    .append(command.description())
                    .append("\n");
            });
            sb.delete(sb.length() - 2, sb.length());

            return new SendMessage(chatID, sb.toString());
        }

        return new SendMessage(chatID, "Такой комманды нет, напишите /help");
    }
}
