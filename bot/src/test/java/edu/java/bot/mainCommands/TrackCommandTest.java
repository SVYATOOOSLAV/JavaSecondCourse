package edu.java.bot.mainCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.commands.mainCommands.TrackCommand;
import edu.java.bot.dao.DataBase;
import edu.java.bot.model.linkvalidators.LinkValidatorManager;
import edu.java.bot.model.linkvalidators.baseValidators.GitHubValidator;
import edu.java.bot.model.linkvalidators.baseValidators.StackOverflowValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackCommandTest {
    long chatID = 123;
    String botName = "testBot";
    LinkValidatorManager manager = new LinkValidatorManager(
        List.of(new GitHubValidator(), new StackOverflowValidator()));
    TrackCommand command = new TrackCommand(manager, botName);

    @AfterEach
    public void resetDatabase() {
        DataBase.dropDB();
    }
    @Test
    void userNotInSystemTest(){
        String referent = "Вы не зарегистрированы в системе, введите команду /start";
        assertEquals(referent, command.handle(chatID, "/track").getParameters().get("text"));
    }

    private static Stream<Arguments> provideTestCasesForTheTask(){
        return Stream.of(
            Arguments.of(
                "/track",
                """
                Give me link or links that you want to track in massage starting with /track
                As an example:
                /track
                https://google.com/
                https://chat.openai.com/""",
                new ArrayList<URI>()
            ),
            Arguments.of(
                "/track https://github.com/SVYATOOOSLAV",
                "Link was added to the tracklist. If you want to see it send /list",
                List.of(URI.create("https://github.com/SVYATOOOSLAV"))
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""",
                "All links were added to the the tracklist. If you want to see it send /list",
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                )
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link
                https://unsupportedlink.ru/test
                invalid/link""",
                """
                Links were added to the tracklist: https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link
                If want to see all tracklist send /list
                Invalid links:
                https://unsupportedlink.ru/test
                invalid/link
                """,
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                ),
                Arguments.of(
                    """
                    /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                    https://unsupportedlink.ru/test
                    invalid/link""",
                    """
                    Link was added to the tracklist: https://github.com/sanyarnd/tinkoff-java-course-2023/
                    If want to see all tracklist send /list
                    Invalid links:
                    https://unsupportedlink.ru/test
                    invalid/link""",
                    List.of(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"))
                ),
                Arguments.of(
                    """
                    /track
                    https://unsupportedlink.ru/test
                    invalid/link""",
                    """
                    Bot supports the following resources:\s
                    github.com
                    stackoverflow.com
                    """,
                    new ArrayList<URI>()
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForTheTask")
    void userInSystemTest(String testCase, String referent, List<URI> links){
        DataBase.createUser(123L);
        SendMessage message = command.handle(chatID, testCase);
        assertEquals(referent, message.getParameters().get("text"));
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForTheTask")
    void userLinksTest(String testCase, String answer, List<URI> referent){
        DataBase.createUser(123L);
        command.handle(123L, testCase);
        assertEquals(referent, DataBase.getUserTrackList(123L));
    }
    @Test
    public void simplyTest() {
       String testCase =
            """
            /track
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link""";

        DataBase.createUser(123L);
        SendMessage testResult = command.handle(123L, testCase);
        String referent = """
            Links were added to the tracklist: https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link
            If want to see all tracklist send /list

            """;
        List<URI> databaseReferent = List.of(
            URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
            URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
            URI.create("https://stackoverflow.com/search?q=unsupported%20link")
        );
        assertEquals(databaseReferent, DataBase.getUserTrackList(123L));
        assertEquals(referent, testResult.getParameters().get("text"));
    }

}
