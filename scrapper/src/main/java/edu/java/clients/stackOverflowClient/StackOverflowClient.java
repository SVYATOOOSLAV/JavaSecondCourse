package edu.java.clients.stackOverflowClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.clients.ClientMaker;
import edu.java.clients.JsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import java.util.Optional;
@Component
public class StackOverflowClient implements JsonParser<StackOverflowResponse> {
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.stackexchange.com/2.3/questions";

    private static final String requestParam = "?order=desc&sort=activity&site=stackoverflow";
    public StackOverflowClient(){
        webClient = ClientMaker.getWebClient(BASE_URL);
    }
    public StackOverflowClient(String baseURL){
        webClient = ClientMaker.getWebClient(baseURL);
    }

    public Optional<StackOverflowResponse> fetchQuestion(long questionID){
        String data = webClient.get()
            .uri("/{id}" + requestParam, questionID)
            //.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return Optional.of(convertJsonToObject(data));
    }
    @Override
    public StackOverflowResponse convertJsonToObject(String data) {
        ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode hashMap = mapper.readTree(data); //get hashmap
            JsonNode values = hashMap.get("items"); // get values from hashmap
            JsonNode arr = values.get(0);
            return mapper.treeToValue(arr, StackOverflowResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
