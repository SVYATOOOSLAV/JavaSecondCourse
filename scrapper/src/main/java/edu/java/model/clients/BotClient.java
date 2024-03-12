package edu.java.model.clients;

import edu.java.DTO.exception.DataNotFoundException;
import edu.java.DTO.exception.InvalidRequestException;
import edu.java.DTO.request.LinkUpdate;
import edu.java.DTO.response.ApiErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Optional;

public class BotClient {
    private final static String BASE_URL = "http://localhost:8090";
    private final WebClient webClient;

    public BotClient() {
        webClient = ClientMaker.getWebClient(BASE_URL);
    }

    @Autowired
    public BotClient(String baseURL) {
        webClient = ClientMaker.getWebClient(baseURL);
    }

    public Optional<Exception> sendUpdate(LinkUpdate update){
        try{
            webClient.post()
                .uri("/updates")
                .bodyValue(update)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    status -> status.bodyToMono(ApiErrorResponse.class)
                        .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
                )
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e){
            return Optional.of(e);
        }
        return Optional.empty();
    }
}
