package edu.java.scrapper.clients;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Optional;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.clients.gitHubClient.GitHubClient;
import edu.java.clients.gitHubClient.GitHubResponse;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubClientTest {
    private WireMockServer wireMockServer;

    private GitHubClient client;

    @DisplayName("ValidDataTest")
    @Test
    void simpleTestToGetInfoAboutRepo() throws IOException {
        String repoOwner = "SVYATOOOSLAV";
        String repoName = "JavaSecondCourse";
        Path pathToJson = Path.of("src","test","java","edu","java","scrapper","clients","GithubTest.json");
        makeStubServer(repoOwner, repoName,pathToJson);
        GitHubResponse referent = new GitHubResponse(
            752695166,
            "JavaSecondCourse",
            false,
            new GitHubResponse.Owner(
                "SVYATOOOSLAV",
                138962302,
                "https://avatars.githubusercontent.com/u/138962302?v=4",
                "https://github.com/SVYATOOOSLAV"
            ),
            "https://github.com/SVYATOOOSLAV/JavaSecondCourse",
            null,
            OffsetDateTime.parse("2024-02-04T14:57:07Z"),
            OffsetDateTime.parse("2024-02-04T14:57:48Z"),
            OffsetDateTime.parse("2024-03-03T16:12:54Z"),
            "git://github.com/SVYATOOOSLAV/JavaSecondCourse.git",
            "Java"
        );
        GitHubResponse response = client.fetchRepository(repoOwner, repoName).get();
        assertEquals(response, referent);
    }

    @DisplayName("WithoutJSON")
    @Test
    void failedWithoutJsonTest(){
        String repoOwner = "SVYATOOOSLAV";
        String repoName = "JavaSecondCourse";
        String json = "";
        makeStubServer(repoOwner, repoName, json);
        Optional<GitHubResponse> response =  client.fetchRepository(repoOwner, repoName);
        assertTrue(response.isEmpty());
    }

    @DisplayName("InvalidRepo")
    @Test
    void failedWithInvalidRepoTest(){
        String repoOwner = "test";
        String repoName = "test";
        wireMockServer.stubFor(
            get(urlEqualTo(String.format("/%s/%s", repoOwner, repoName)))
                .willReturn(aResponse()
                    .withStatus(HttpStatus.NOT_FOUND.value())
                    .withBody("")));
        Optional<GitHubResponse> response =  client.fetchRepository(repoOwner, repoName);
        assertTrue(response.isEmpty());
    }

    @BeforeEach
    void clientServerStart() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        client = new GitHubClient("http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    void serverStop() {
        wireMockServer.stop();
    }

    private void makeStubServer(String repoOwner, String repoName, Path pathToJson) throws IOException {
         wireMockServer.stubFor(
            get(urlEqualTo(String.format("/%s/%s", repoOwner, repoName)))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withJsonBody(new ObjectMapper().readTree(pathToJson.toFile())))
        );
    }

    private void makeStubServer(String repoOwner, String repoName, String jsonResponse) {
        wireMockServer.stubFor(
            get(urlEqualTo(String.format("/%s/%s", repoOwner, repoName)))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(jsonResponse))
        );
    }
}
