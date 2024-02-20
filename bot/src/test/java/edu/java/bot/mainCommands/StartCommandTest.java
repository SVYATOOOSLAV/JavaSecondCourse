package edu.java.bot.mainCommands;

import edu.java.bot.commands.mainCommands.StartCommand;
import edu.java.bot.dao.DataBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartCommandTest {

    StartCommand command = new StartCommand("testBot");

    @AfterEach
    void dropDB() {
        DataBase.dropDB();
    }

    @Test
    void newUserInBotTest() {
        String referent = "Вы успешно зарегистрированы в боте testBot";
        assertEquals(referent, command.handle(123, "/start").getParameters().get("text"));
    }

    @Test
    void repeatAuthorization() {
        String referent = "Вы уже находитесь в системе бота testBot";
        DataBase.createUser(123L);
        assertEquals(referent, command.handle(123, "/start").getParameters().get("text"));
    }
}
