package edu.java.bot.commands.mainCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.dao.DataBase;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.List;

@Component
public class ListCommand implements Command {
    @Override
    public String commandName() {
        return "/list";
    }

    @Override
    public String description() {
        return "Ваши подписки";
    }

    @Override
    public SendMessage handle(long chatID, String text) {

        if (!DataBase.isUserInSystem(chatID)) {
            return new SendMessage(chatID, "Вы не зарегистрированы в системе, введите команду /start");
        }

        if (text.equals(commandName())) {
            List<URI> links = DataBase.getUserTrackList(chatID);
            if (links.isEmpty()) {
                return new SendMessage(chatID, "У вас нет подписок");
            }

            StringBuilder sb = new StringBuilder("Ваши подписки:\n");
            links.forEach(link -> {
                sb.append(link.toString());
                sb.append("\n");
            });
            sb.deleteCharAt(sb.length()-1);
            return new SendMessage(chatID, sb.toString());

        }
        return new SendMessage(chatID, "Введите /list для просмотра ваших подписок");
    }
}
