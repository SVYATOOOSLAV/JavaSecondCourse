package edu.java.bot.mainCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.mainCommands.TrackCommand;
import edu.java.bot.commands.mainCommands.UntrackCommand;
import edu.java.bot.dao.DataBase;
import edu.java.bot.linkvalidators.LinkValidatorManager;
import edu.java.bot.linkvalidators.baseValidators.GitHubValidator;
import edu.java.bot.linkvalidators.baseValidators.StackOverflowValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UntrackCommandTest {
    long chatID = 123L;

    String botName = "testBot";

    LinkValidatorManager manager = new LinkValidatorManager(
        List.of(new GitHubValidator(), new StackOverflowValidator()));
    UntrackCommand untrackCommand = new UntrackCommand(manager, botName);
    TrackCommand trackCommand = new TrackCommand(manager, botName);
    @AfterEach
    public void dropDB(){
        DataBase.dropDB();
    }

    @Test
    public void emptyWithoutAuthorizationTest() {
        SendMessage testResult = untrackCommand.handle(chatID, "/untrack");
        String referent = "Вы не зарегистрированы в системе, введите команду /start";
        assertEquals(referent, testResult.getParameters().get("text"));
    }

    private static Stream<Arguments> providedTestCasesForTheTask() {
        return Stream.of(
            Arguments.of(
                "/track",
                "/untrack",
                """
                Give me link or links that you want to untracked in massage starting with /untrack
                As an example:
                /untrack
                https://google.com/
                https://chat.openai.com/""",
                new ArrayList<URI>(),
                new ArrayList<URI>()
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""",
                """
                /untrack https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""",
                "All links were removed from tracklist. If you want to see it send /list",
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                ),
                new ArrayList<URI>()
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""",
                "/untrack https://github.com/sanyarnd/tinkoff-java-course-2023/",
                "Link was removed from tracklist. If you want to see it send /list",
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                ),
                List.of(
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                )
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://unsupportedlink.ru/test
                invalid/link""",
                """
                /untrack https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link
                https://unsupportedlink.ru/test
                invalid/link""",
                """
                Links were removed from tracklist: https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                If want to see all tracklist send /list
                Invalid links:
                https://stackoverflow.com/search?q=unsupported%20link
                https://unsupportedlink.ru/test
                invalid/link
                """,
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c")
                ),
                new ArrayList<URI>()
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://unsupportedlink.ru/test
                invalid/link""",
                """
                /untrack https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://unsupportedlink.ru/test
                invalid/link""",
                """
                Link was removed from tracklist: https://github.com/sanyarnd/tinkoff-java-course-2023/
                If want to see all tracklist send /list
                Invalid links:
                https://unsupportedlink.ru/test
                invalid/link
                """,
                List.of(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/")),
                new ArrayList<URI>()
            ),
            Arguments.of(
                """
                /track
                https://unsupportedlink.ru/test
                invalid/link""",
                """
                /untrack
                https://unsupportedlink.ru/test
                invalid/link""",
                "Invalid input, check your links with command /list",
                new ArrayList<URI>(),
                new ArrayList<URI>()
            )
        );
    }

    @ParameterizedTest
    @MethodSource("providedTestCasesForTheTask")
    @DisplayName("Test command track and untrack")
    public void userInSystemWithUntrackTest(
        String track,
        String untrack,
        String output,
        List<URI> linksBeforeRemoved,
        List<URI> linksAfterRemoved
    ) {
        DataBase.createUser(chatID);
        trackCommand.handle(chatID, track);
        SendMessage testResult = untrackCommand.handle(chatID, untrack);
        assertEquals(output, testResult.getParameters().get("text"));
    }

    @ParameterizedTest
    @MethodSource("providedTestCasesForTheTask")
    @DisplayName("Links before and after removed")
    public void checkLinksOnUserTest(
        String testInput,
        String testCase,
        String outputReferent,
        List<URI> linksBeforeRemoved,
        List<URI> linksAfterRemoved
    ) {

        DataBase.createUser(chatID);
        trackCommand.handle(chatID, testInput);
        //Check before removed
        assertEquals(linksBeforeRemoved, DataBase.getUserTrackList(chatID));
        //check after removed
        untrackCommand.handle(chatID, testCase);
        assertEquals(linksAfterRemoved, DataBase.getUserTrackList(chatID));
    }


}
