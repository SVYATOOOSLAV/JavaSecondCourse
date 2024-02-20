package edu.java.bot.mainCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.mainCommands.HelpCommand;
import edu.java.bot.commands.mainCommands.ListCommand;
import edu.java.bot.commands.mainCommands.StartCommand;
import edu.java.bot.commands.mainCommands.TrackCommand;
import edu.java.bot.commands.mainCommands.UntrackCommand;
import edu.java.bot.dao.DataBase;
import edu.java.bot.linkvalidators.LinkValidatorManager;
import edu.java.bot.linkvalidators.baseValidators.GitHubValidator;
import edu.java.bot.linkvalidators.baseValidators.StackOverflowValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpCommandTest {

    @BeforeAll
    static void authorize(){
        DataBase.createUser(123L);
    }
    LinkValidatorManager manager = new LinkValidatorManager(
        List.of(new GitHubValidator(), new StackOverflowValidator()));
    private final List<Command> commands = List.of(
        new ListCommand(),
        new StartCommand("testBot"),
        new TrackCommand(manager, "testBot"),
        new UntrackCommand(manager, "testBot")
    );


    @Test
    void simpleTest(){
        String referent =
            """
            Список существующих команд:
            /list: Ваши подписки
            /start: Зарегистрироваться в боте
            /track: Подписаться на ресурс
            /untrack: Отписаться от ресурса / ресурсов""";

        HelpCommand helpCommand = new HelpCommand(commands);
        SendMessage response = helpCommand.handle(123, "/help");
        assertEquals(referent, response.getParameters().get("text"));
    }
}
