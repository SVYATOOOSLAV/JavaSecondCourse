package edu.java.bot;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.mainCommands.ListCommand;
import edu.java.bot.commands.mainCommands.StartCommand;
import edu.java.bot.commands.mainCommands.TrackCommand;
import edu.java.bot.commands.mainCommands.UntrackCommand;
import edu.java.bot.dao.DataBase;
import edu.java.bot.linkvalidators.LinkValidatorManager;
import edu.java.bot.linkvalidators.baseValidators.GitHubValidator;
import edu.java.bot.linkvalidators.baseValidators.StackOverflowValidator;
import edu.java.bot.processor.CommandProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandProcessorTest {
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

    CommandProcessor commandProcessor = new CommandProcessor(commands);

    @Test
    public void invalidProcessInputTest() {
        String referent = "Command not found, send me command /help";
        assertEquals(referent, commandProcessor.process(123L, "testCase").getParameters().get("text"));
    }
}
