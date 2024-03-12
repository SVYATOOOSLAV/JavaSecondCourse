package edu.java.model.clients.gitHubClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubResponse(
    long id,
    String name,
    @JsonProperty("private")
    boolean isPrivate,
    @JsonProperty("owner")
    Owner repoOwner,
    @JsonProperty("html_url")
    String htmlURL,
    String description,
    @JsonProperty("created_at")
    OffsetDateTime createdAt,
    @JsonProperty("updated_at")
    OffsetDateTime updatedAt,
    @JsonProperty("pushed_at")
    OffsetDateTime pushedAt,
    @JsonProperty("git_url")
    String gitURL,
    String language
) {
    public record Owner(
        String login,
        long id,
        @JsonProperty("avatar_url")
        String avatarURL,
        @JsonProperty("html_url")
        String htmlURL
    ) {
    }
}


