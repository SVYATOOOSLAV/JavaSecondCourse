package edu.java.bot.model.objects.clients;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMaker {
    public static WebClient getWebClient(String baseURL){
        return createWebClient(baseURL);
    }
    private static WebClient createWebClient(String baseURL) {
        return WebClient.builder()
            .baseUrl(baseURL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
