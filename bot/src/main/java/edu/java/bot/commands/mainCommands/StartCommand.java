package edu.java.bot.commands.mainCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.dao.DataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

    String botName;

    @Autowired
    public StartCommand(String botName){
        this.botName = botName;
    }
    @Override
    public String commandName() {
        return "/start";
    }

    @Override
    public String description() {
        return "Зарегистрироваться в боте";
    }

    @Override
    public SendMessage handle(long chatID, String text) {
        if(text.equals(commandName())){
            if(DataBase.isUserInSystem(chatID)){
                return new SendMessage(chatID, "Вы уже находитесь в системе бота " + botName);
            }
            DataBase.createUser(chatID);
            return new SendMessage(chatID, "Вы успешно зарегистрированы в боте " + botName);
        }
        return new SendMessage(chatID, "Введите /start, чтобы зарегистрироваться");
    }
}
