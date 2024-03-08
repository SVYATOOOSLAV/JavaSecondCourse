package edu.java.clients.stackOverflowClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowResponse(
    @JsonProperty("owner")
    Owner questionOwner,
    @JsonProperty("is_answered")
    boolean isAnswered,
    @JsonProperty("view_count")
    long viewCount,
    @JsonProperty("answer_count")
    long answerCount,
    int score,
    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivityDate,
    @JsonProperty("creation_date")
    OffsetDateTime creationDate,
    @JsonProperty("last_edit_date")
    OffsetDateTime lastEditDate,
    @JsonProperty("question_id")
    long questionID,
    String link,
    String title

) {
    public record Owner(
        @JsonProperty("profile_image")
        String profileImage,
        @JsonProperty("display_name")
        String displayName,
        String link
    ) {
    }
}
