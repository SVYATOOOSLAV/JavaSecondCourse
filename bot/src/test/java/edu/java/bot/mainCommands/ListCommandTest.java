package edu.java.bot.mainCommands;

import edu.java.bot.model.commands.mainCommands.ListCommand;
import edu.java.bot.dao.DataBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest {
    @BeforeAll
    static void autorize() {
        DataBase.createUser(123L);
        DataBase.addLinksToUser(123L, List.of(URI.create("https://github.com/SVYATOOOSLAV")));
    }

    @Test
    void simpleTest(){
        String referent =
            """
            Ваши подписки:
            https://github.com/SVYATOOOSLAV""";
        ListCommand command = new ListCommand();
        assertEquals(referent, command.handle(123, "/list").getParameters().get("text"));
    }

}
