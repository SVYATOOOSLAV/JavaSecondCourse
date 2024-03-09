package edu.java.model.clients.gitHubClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.model.clients.ClientMaker;
import edu.java.model.clients.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Optional;

@Component
public class GitHubClient implements JsonParser<GitHubResponse> {
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.github.com/repos";

    public GitHubClient() {
        webClient = ClientMaker.getWebClient(BASE_URL);
    }

    public GitHubClient(String baseURL) {
        webClient = ClientMaker.getWebClient(baseURL);
    }

    public Optional<GitHubResponse> fetchRepository(String repoOwner, String repoName) {
        try {
            String data = webClient.get()
                .uri(String.format("/%s/%s", repoOwner, repoName))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return Optional.of(convertJsonToObject(data));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @Override
    public GitHubResponse convertJsonToObject(String data) {
        ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(data, GitHubResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
