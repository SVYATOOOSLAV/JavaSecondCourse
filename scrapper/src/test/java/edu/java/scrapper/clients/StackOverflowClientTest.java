package edu.java.scrapper.clients;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.model.clients.stackOverflowClient.StackOverflowClient;
import edu.java.model.clients.stackOverflowClient.StackOverflowResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackOverflowClientTest {
    WireMockServer wireMockServer;

    StackOverflowClient client;

    @DisplayName("ValidTest")
    @Test
    void simpleTest() throws IOException {
        long id = 11227809;
        Path pathToJson = Path.of("src",
            "test","java","edu","java","scrapper","clients","StackOverflowTest.json");
        createStub(id, pathToJson);
        StackOverflowResponse referent = new StackOverflowResponse(
            new StackOverflowResponse.Owner(
                "https://i.stack.imgur.com/FkjBe.png?s=256&g=1",
                "GManNickG",
                "https://stackoverflow.com/users/87234/gmannickg"
            ),
            true,
            1870715,
            25,
            27213,
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1709573833), ZoneId.of("UTC")),
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1340805096), ZoneId.of("UTC")),
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1701123268), ZoneId.of("UTC")),
            11227809,
            "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-processing-an-unsorted-array",
            "Why is processing a sorted array faster than processing an unsorted array?"
        );
        StackOverflowResponse response = client.fetchQuestion(id).get();
        assertEquals(response, referent);
    }

    @DisplayName("WithoutJSON")
    @Test
    void failedWithoutJsonTest() throws IOException {
        long id = 11227809;
        String json = "";
        createStub(id, json);
        Optional<StackOverflowResponse> response = client.fetchQuestion(id);
        assertTrue(response.isEmpty());
    }

    @DisplayName("InvalidID")
    @Test
    void invalidIDTest() throws IOException {
        long id = -1;
        wireMockServer.stubFor(
            get(urlEqualTo(String.format("/%d?order=desc&sort=activity&site=stackoverflow", id)))
                .willReturn(aResponse()
                    .withStatus(404)
                    .withBody("")));
        Optional<StackOverflowResponse> response = client.fetchQuestion(id);
        assertTrue(response.isEmpty());
    }


    @BeforeEach
    void clientServerStart() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
        client = new StackOverflowClient("http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    void serverStop() {
        wireMockServer.stop();
    }
    private void createStub(long id, Path pathToJson) throws IOException {
        wireMockServer.stubFor(
            get(urlEqualTo(String.format("/%d?order=desc&sort=activity&site=stackoverflow", id)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withJsonBody(new ObjectMapper().readTree(pathToJson.toFile()))));
    }
    private void createStub(long id, String pathToJson) throws IOException {
        wireMockServer.stubFor(
            get(urlEqualTo(String.format("/%d?order=desc&sort=activity&site=stackoverflow", id)))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(pathToJson)));
    }

}
