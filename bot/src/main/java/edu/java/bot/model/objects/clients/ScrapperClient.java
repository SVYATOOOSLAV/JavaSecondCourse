package edu.java.bot.model.objects.clients;

import edu.java.DTO.exception.DataNotFoundException;
import edu.java.DTO.exception.InvalidRequestException;
import edu.java.DTO.request.LinkRequest;
import edu.java.DTO.response.ApiErrorResponse;
import edu.java.DTO.response.LinkResponse;
import edu.java.DTO.response.ListLinksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Optional;

public class ScrapperClient {
    private final WebClient webClient;
    private static final String BASE_URL = "http://localhost:8080";

    public ScrapperClient() {
        webClient = ClientMaker.getWebClient(BASE_URL);
    }

    @Autowired
    public ScrapperClient(String baseURL) {
        webClient = ClientMaker.getWebClient(baseURL);
    }

    public Optional<Exception> addUser(long userID) {
        return workOnUser(HttpMethod.POST, userID);
    }

    public Optional<Exception> deleteUser(long userID) {
        return workOnUser(HttpMethod.DELETE, userID);
    }

    public Optional<LinkResponse> addLink(long userID, String link){
        return workWithLink(HttpMethod.POST, userID, link);
    }

    public Optional<LinkResponse> removeLink(long userID, String link){
        return workWithLink(HttpMethod.DELETE, userID, link);
    }

    public Optional<ListLinksResponse> getTrackList(long userID){
        return webClient.get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(userID))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
            )
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new DataNotFoundException(apiErrorResponse.exceptionMessage()))
            )
            .bodyToMono(ListLinksResponse.class)
            .blockOptional();
    }

    private Optional<Exception> workOnUser(HttpMethod method, long userID) {
        try {
            webClient.method(method)
                .uri(String.format("/tg-chat/%s", userID))
                .retrieve()
                .onStatus(
                    HttpStatus.BAD_REQUEST::equals,
                    clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                        .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
                )
                .onStatus(
                    HttpStatus.NOT_FOUND::equals,
                    clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                        .map(apiErrorResponse -> new DataNotFoundException(apiErrorResponse.exceptionMessage()))
                )
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e) {
            return Optional.of(e);
        }
        return Optional.empty();
    }

    private Optional<LinkResponse> workWithLink(HttpMethod method,long userID, String link){
       return  webClient.method(method)
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(userID))
            .bodyValue(new LinkRequest(link))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                    clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                        .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
            )
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new DataNotFoundException(apiErrorResponse.exceptionMessage()))
            )
            .bodyToMono(LinkResponse.class)
            .blockOptional();
    }
}
